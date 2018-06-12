package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.DynamicAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.DynamicBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.helper.IntentRequestCode;
import com.jyt.baseapp.itemDecoration.SpacesItemDecoration;
import com.jyt.baseapp.model.DynamicModel;
import com.jyt.baseapp.model.impl.DynamicModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.viewholder.DynamicViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DynamicActivity extends BaseMCVActivity {

    @BindView(R.id.trl_dynamic_load)
    TwinklingRefreshLayout mTrlLoad;
    @BindView(R.id.rv_dynamic_content)
    RecyclerView mRvContent;
    private List<DynamicBean> mDynamicList;
    private DynamicAdapter mDynamicAdapter;
    private DynamicModel mDynamicModel;
    private int mPage;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic;
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
        setTextTitle("动态");
        setFunctionText("发布");
        showFunctionText();
        setvMainBackground(R.mipmap.bg_entrance);
        mDynamicModel = new DynamicModelImpl();
        mDynamicModel.onStart(this);
        mDynamicAdapter = new DynamicAdapter();
        mDynamicList = new ArrayList<>();

    }

    private void initSetting() {
        mDynamicAdapter.setDataList(mDynamicList);
        mRvContent.setAdapter(mDynamicAdapter);
        mRvContent.addItemDecoration(new SpacesItemDecoration(0, BaseUtil.dip2px(13)));
        mRvContent.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {
                IntentHelper.OpenReleaseActivityForResult(DynamicActivity.this);
            }
        });
        mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPage = 0;
                mDynamicModel.getDynamic(mPage, 5, new BeanCallback<BaseJson<List<DynamicBean>>>() {
                    @Override
                    public void response(boolean success, BaseJson<List<DynamicBean>> response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                mDynamicList = response.getData();
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTrlLoad.finishRefreshing();
                                mDynamicAdapter.notifyData(mDynamicList);
                            }
                        }, 1500);

                    }
                });
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mDynamicModel.getDynamic(mPage, 5, new BeanCallback<BaseJson<List<DynamicBean>>>() {
                    @Override
                    public void response(boolean success, BaseJson<List<DynamicBean>> response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                if (response.getData()!=null && response.getData().size()>0){
                                    mDynamicList.addAll(response.getData());
                                    mPage++;
                                }
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTrlLoad.finishLoadmore();
                                mDynamicAdapter.notifyData(mDynamicList);
                            }
                        }, 1500);

                    }
                });

            }
        });
        mDynamicAdapter.setOnImageClickListener(new DynamicViewHolder.OnImageClickListener() {
            @Override
            public void OnClick(List<String> data, int index) {
                IntentHelper.openBrowseImagesActivity(DynamicActivity.this,data,index);
            }

            @Override
            public void OnZangClick(int id, boolean flag) {
                if (flag){
                    mDynamicModel.SubmitZang(id , new BeanCallback() {
                        @Override
                        public void response(boolean success, Object response, int id) {

                        }
                    });
                }else {
                    mDynamicModel.CancelZang(id , new BeanCallback() {
                        @Override
                        public void response(boolean success, Object response, int id) {

                        }
                    });
                }
            }

            @Override
            public void OpenVideo(String path) {
                IntentHelper.OpenVideoActivity(DynamicActivity.this,path);
            }
        });
        mTrlLoad.startRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCode.CODE_RELEASE && resultCode ==RESULT_OK){
            mTrlLoad.startRefresh();
        }
    }
}
