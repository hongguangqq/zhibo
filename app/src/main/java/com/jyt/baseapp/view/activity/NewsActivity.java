package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.NewAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.AppointBean;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.CallRecordBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
import com.jyt.baseapp.model.AppointModel;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.AppointModelImpl;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.HawkUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.viewholder.NewViewHolder2;
import com.jyt.baseapp.view.viewholder.NewViewHolder5;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class NewsActivity extends BaseMCVActivity {

    @BindView(R.id.rv_trl_tabnew)
    RecyclerView mRvContent;
    @BindView(R.id.trl_tabnew)
    TwinklingRefreshLayout mTrlLoad;


    private int mCode;
    private AppointModel mAppointModel;
    private PersonModel mPersonModel;
    private NewAdapter mAdapter;
    private List mDataList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_tnew;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tuple tuple = IntentHelper.NewActivityGetPara(getIntent());
        mCode = (int) tuple.getItem1();
        init();
        initSetting();
    }

    private void init() {
        setvMainBackgroundColor(R.color.bg_content);
        mAdapter = new NewAdapter(mCode);
        mDataList = new ArrayList<>();
        mAppointModel = new AppointModelImpl();
        mAppointModel.onStart(this);
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);

    }

    private void initSetting() {
        mTrlLoad.setEnableLoadmore(false);//不可上拉加载更多
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRvContent.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.line_color2)));
        mAdapter.setDataList(mDataList);
        mRvContent.setAdapter(mAdapter);
        switch (mCode){
            case 1:
                setTextTitle("好友消息");
                mDataList = HawkUtil.getFriendtList();
                mAdapter.notifyData(mDataList);
                break;
            case 2:
                setTextTitle("谁看过我");
                mAppointModel.getWhoLokeMe(new BeanCallback<BaseJson<List<UserBean>>>() {
                    @Override
                    public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                       if (success && response.getCode()==200){
                           mDataList = response.getData();
                           mAdapter.notifyData(mDataList);
                       }
                    }
                });
                break;
            case 3:
                setTextTitle("系统通知");
                mDataList = HawkUtil.getPushList();
                mAdapter.notifyData(mDataList);
                break;
            case 4:
                setTextTitle("通话记录");
                mAppointModel.getInOut(new BeanCallback<BaseJson<List<CallRecordBean>>>(this,true,"") {
                    @Override
                    public void response(boolean success, BaseJson<List<CallRecordBean>> response, int id) {
                        if (success && response.getCode()==200){
                            mDataList = response.getData();
                            mAdapter.notifyData(mDataList);
                        }
                    }
                });
                break;
            case 5:
                setTextTitle("我的预约");
                mAppointModel.getMyOrder(new BeanCallback<BaseJson<List<AppointBean>>>() {
                    @Override
                    public void response(boolean success, BaseJson<List<AppointBean>> response, int id) {
                        if (success && response.getCode()==200){
                            mDataList = response.getData();
                            mAdapter.notifyData(mDataList);
                        }
                    }
                });
                break;

        }
        mAdapter.setOnAppointListener(new NewViewHolder5.OnAppointListener() {
            @Override
            public void CallBack(int id) {
                //回拨
                mPersonModel.getUserData(id, new BeanCallback<BaseJson<UserBean>>(NewsActivity.this,true,null) {
                    @Override
                    public void response(boolean success, BaseJson<UserBean> response, int id) {
                        if (success && response.getCode() == 200){
                            ScannerManager.isRingBack = true;
                            UserBean user = response.getData();
                            ScannerManager.comID = user.getEasyId();
                            ScannerManager.uId = String.valueOf(user.getId());
                        }
                    }
                });
            }

            @Override
            public void CancelOrder(final int id) {
                //取消预约
                IPhoneDialog dialog = new IPhoneDialog(NewsActivity.this);
                dialog.setTitle("确认取消?");
                dialog.setInputShow(false);
                dialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
                    @Override
                    public void ClickSubmit(boolean isShow, String input) {
                        mAppointModel.CancelOrder(id, new BeanCallback() {
                            @Override
                            public void response(boolean success, Object response, int id) {

                            }
                        });
                    }

                    @Override
                    public void ClickCancel() {
                        Log.e("@#","ClickCancel");
                    }
                });
                dialog.show();
            }
        });

        mAdapter.setsetOnOpenVideoListener(new NewViewHolder2.OnOpenVideoListener() {
            @Override
            public void Open(UserBean user) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppointModel.onDestroy();
        mPersonModel.onDestroy();
    }
}
