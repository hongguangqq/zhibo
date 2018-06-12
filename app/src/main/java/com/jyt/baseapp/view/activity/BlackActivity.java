package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.BlackAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class BlackActivity extends BaseMCVActivity {

    @BindView(R.id.rv_ff)
    RecyclerView mRvContent;
    @BindView(R.id.trl_ff)
    TwinklingRefreshLayout mTrlLoad;
    private BlackAdapter mAdapter;
    private List<UserBean> mBlackList;
    private PersonModel mPersonModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ff;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initSetting();
    }

    private void init() {
        setTextTitle("黑名单列表");
        setvMainBackgroundColor(R.color.bg_content);
        mAdapter = new BlackAdapter();
        mBlackList =new ArrayList<>();
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mAdapter.setDataList(mBlackList);
        mRvContent.setAdapter(mAdapter);
    }

    private void initSetting() {
        mTrlLoad.setEnableLoadmore(false);//不可上拉加载更多
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvContent.addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.line_color2)));
        getData();
        mAdapter.setOnViewHolderLongClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                final UserBean user = (UserBean) holder.getData();
                final IPhoneDialog dialog = new IPhoneDialog(BlackActivity.this);
                dialog.setTitle("确定移除？");
                dialog.setInputShow(false);
                dialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
                    @Override
                    public void ClickSubmit(boolean isShow, String input) {
                        mPersonModel.DeleteBlackList(user.getId(), new BeanCallback<BaseJson>() {
                            @Override
                            public void response(boolean success, BaseJson response, int id) {
                                if (success && response.getCode()==200){
                                    getData();
                                    BaseUtil.makeText("移除成功");
                                }else {
                                    BaseUtil.makeText("移除失败，请重试");
                                }
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void ClickCancel() {

                    }
                });
                dialog.show();
            }
        });
    }

    private void getData(){
        mPersonModel.GetBlackList(new BeanCallback<BaseJson<List<UserBean>>>() {
            @Override
            public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                if (success && response.getCode()==200){
                    mBlackList = response.getData();
                    if (mBlackList!=null){
                        mAdapter.notifyData(mBlackList);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonModel.onDestroy();
    }
}
