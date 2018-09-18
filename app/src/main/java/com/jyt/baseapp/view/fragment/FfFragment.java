package com.jyt.baseapp.view.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FFAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.FFViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 10:34
 */
public class FfFragment extends BaseFragment {
    @BindView(R.id.rv_ff)
    RecyclerView mRvContent;
    @BindView(R.id.trl_ff)
    TwinklingRefreshLayout mTrlLoad;

    private PersonModel mPersonModel;
    private FFAdapter mAdapter;
    private List<UserBean> mDataList;
    private int mPage;
    private int mStatePath;//请求数据的地址 0/我的关注 1/关注我的
    private Handler mHandler = new Handler();

    public FfFragment(){
        super();
    }

    public FfFragment(int path){
        mStatePath = path;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ff;
    }

    @Override
    protected void firstInit() {
        init();
        initSetting();
    }


    private void init() {
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(getActivity());
        mAdapter = new FFAdapter();
        mDataList = new ArrayList<>();

    }

    private void initSetting() {
        mTrlLoad.setEnableLoadmore(false);//不可上拉加载更多
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvContent.addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.line_color2)));
        mAdapter.setDataList(mDataList);
        mAdapter.setOnVideoClickListener(new FFViewHolder.OnVideoClickListener() {
            @Override
            public void OnClick(UserBean data) {
                IntentHelper.OpenLaunchActivity(getActivity(),data.getId(),2,data.getNickname(),data.getHeadImg());
            }
        });
        mAdapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                UserBean bean = (UserBean) holder.getData();
                IntentHelper.OpenPersonActivity(getActivity(),bean.getId());
            }
        });
        mRvContent.setAdapter(mAdapter);
        getData();
        mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPage=0;
                if (mStatePath == 0){
                    mPersonModel.GetFollowList(mPage, 6, new BeanCallback<BaseJson<List<UserBean>>>() {
                        @Override
                        public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                            if (success && response.getCode()==200){
                                if (response.getData()!=null && response.getData().size()>0){
                                    mDataList = response.getData();

                                }
                            }
                        }
                    });
                }else if (mStatePath==1){
                    mPersonModel.GetFansList(mPage, 6, new BeanCallback<BaseJson<List<UserBean>>>() {
                        @Override
                        public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                            if (success && response.getCode()==200){
                                if (response.getData()!=null && response.getData().size()>0){
                                    mDataList = response.getData();

                                }
                            }
                        }
                    });
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyData(mDataList);
                        BaseUtil.makeText("刷新成功");
                    }
                }, 1500);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mStatePath == 0){
                    mPersonModel.GetFollowList(mPage, 6, new BeanCallback<BaseJson<List<UserBean>>>() {
                        @Override
                        public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                            if (success && response.getCode()==200){
                                if (response.getData()!=null && response.getData().size()>0){
                                    mDataList = response.getData();
                                    mPage++;
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyData(mDataList);
                                            BaseUtil.makeText("加载成功");
                                        }
                                    }, 1500);
                                }else {
                                    BaseUtil.makeText("没有更多数据");
                                }
                            }
                        }
                    });
                }else if (mStatePath==1){
                    mPersonModel.GetFansList(mPage, 6, new BeanCallback<BaseJson<List<UserBean>>>() {
                        @Override
                        public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                            if (success && response.getCode()==200){
                                if (response.getData()!=null && response.getData().size()>0){
                                    mDataList = response.getData();
                                    mPage++;
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyData(mDataList);
                                            BaseUtil.makeText("加载成功");
                                        }
                                    }, 1500);
                                }else {
                                    BaseUtil.makeText("没有更多数据");
                                }
                            }
                        }
                    });
                }

            }
        });
    }

    private void getData(){
        if (mStatePath == 0){
            mPersonModel.GetFollowList(mPage, 6, new BeanCallback<BaseJson<List<UserBean>>>() {
                @Override
                public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                    if (success && response.getCode()==200){
                        if (response.getData()!=null && response.getData().size()>0){
                            mDataList = response.getData();
                            mAdapter.notifyData(mDataList);
                            mPage++;
                        }
                    }
                }
            });
        }else if (mStatePath==1){
            mPersonModel.GetFansList(mPage, 6, new BeanCallback<BaseJson<List<UserBean>>>() {
                @Override
                public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                    if (success && response.getCode()==200){
                        if (response.getData()!=null && response.getData().size()>0){
                            mDataList = response.getData();
                            mAdapter.notifyData(mDataList);
                            mPage++;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPersonModel.onDestroy();
    }
}
