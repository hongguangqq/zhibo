package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.ListAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.HotBean;
import com.jyt.baseapp.bean.SearchBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.TabModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.model.impl.TabModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListActivity extends BaseMCVActivity {

    @BindView(R.id.rv_list_content)
    RecyclerView mRvContent;
    @BindView(R.id.trl_list_load)
    TwinklingRefreshLayout mTrlLoad;



    private int mPage;
    private int PageCode;
    private TabModel mTabModel;
    private PersonModel mPersonModel;
    private List<UserBean> mDataList;
    private ListAdapter mListAdapter;


    private Handler mHandler = new Handler();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_list;
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

    private void init(){
        Tuple tuple = IntentHelper.ListActivityGetPara(getIntent());
        PageCode = (int) tuple.getItem1();
        setvActionBarColor(R.color.bg_content);
        mTabModel = new TabModelImpl();
        mTabModel.onStart(this);
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mDataList = new ArrayList<>();
        mListAdapter = new ListAdapter();
        switch (PageCode){
            case 1:
                setTextTitle("关注列表");
                break;
            case 2:
                setTextTitle("热门列表");
                break;
            case 3:
                setTextTitle("用户列表");
                break;
            case 4:
                setTextTitle("推荐列表");
                break;
        }

        setData();
    }

    private void initSetting(){
        mRvContent.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void setData(){
        if (PageCode == 3){
            //男女用户列表
            mTabModel.getListData(PageCode,mPage, 10, new BeanCallback<BaseJson<SearchBean>>(this,false,null) {
                @Override
                public void response(boolean success, BaseJson<SearchBean> response, int id) {
                    if (success){
                        if (response.getCode()==200 && response.getData().getContent()!=null && response.getData().getContent().size()>0){
                            mDataList.addAll(response.getData().getContent());
                            mListAdapter.setDataList(mDataList);
                            mRvContent.setAdapter(mListAdapter);
                            mPage++;
                        }
                    }
                }
            });

            mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
                @Override
                public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                    super.onRefresh(refreshLayout);
                    mPage=0;
                    mTabModel.getListData(PageCode,mPage, 10, new BeanCallback<BaseJson<SearchBean>>() {
                        @Override
                        public void response(boolean success, BaseJson<SearchBean> response, int id) {
                            if (success){
                                if (response.getCode()==200 && response.getData().getContent()!=null && response.getData().getContent().size()>0){
                                    mDataList = response.getData().getContent();
                                    mListAdapter.setDataList(mDataList);
                                    mPage++;
                                }
                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mTrlLoad.finishRefreshing();
                                    mListAdapter.notifyDataSetChanged();
                                    BaseUtil.makeText("刷新完毕");
                                }
                            }, 1500);
                        }

                    });

                }

                @Override
                public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                    super.onLoadMore(refreshLayout);
                    mTabModel.getListData(PageCode,mPage, 10, new BeanCallback<BaseJson<SearchBean>>() {
                        @Override
                        public void response(boolean success, BaseJson<SearchBean> response, int id) {
                            if (success){
                                if (response.getCode()==200 ){
                                    if (response.getData().getContent()!=null && response.getData().getContent().size()>0){
                                        mDataList.addAll(response.getData().getContent());
                                        mListAdapter.setDataList(mDataList);
                                        mPage++;
//                                        BaseUtil.makeText("加载完毕");
                                    }else {
                                        BaseUtil.makeText("没有更多数据");
                                        mTrlLoad.finishLoadmore();
                                        return;
                                    }
                                }
                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mTrlLoad.finishLoadmore();
                                    mListAdapter.notifyDataSetChanged();

                                }
                            }, 1500);
                        }
                    });
                }
            });
        } else if (PageCode == 2){
            //热门
            mTabModel.getListData(PageCode, mPage, 10, new BeanCallback<BaseJson<List<HotBean>>>() {
                @Override
                public void response(boolean success, BaseJson<List<HotBean>> response, int id) {
                    if (success && response.getCode()==200){
                        for(HotBean hot:response.getData()){
                            mDataList.add(hot.getUser());
                        }
                        mListAdapter.setDataList(mDataList);
                        mListAdapter.notifyDataSetChanged();
                        mRvContent.setAdapter(mListAdapter);
                    }
                }
            });


        } else {
            mTabModel.getListData(PageCode,mPage, 10, new BeanCallback<BaseJson<List<UserBean>>>(this,false,null) {
                @Override
                public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                    if (success){
                        if (response.getCode()==200 && response.getData()!=null && response.getData().size()>0){
                            mDataList.addAll(response.getData());
                            mListAdapter.setDataList(mDataList);
                            mRvContent.setAdapter(mListAdapter);
                            mPage++;
                        }
                    }
                }
            });

            mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
                @Override
                public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                    super.onRefresh(refreshLayout);
                    mPage=0;
                    mTabModel.getListData(PageCode,mPage, 10, new BeanCallback<BaseJson<List<UserBean>>>() {
                        @Override
                        public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                            if (success){
                                if (response.getCode()==200 && response.getData()!=null && response.getData().size()>0){
                                    mDataList = response.getData();
                                    mListAdapter.setDataList(mDataList);
                                    mPage++;
                                }
                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mTrlLoad.finishRefreshing();
                                    mListAdapter.notifyDataSetChanged();
                                    BaseUtil.makeText("刷新完毕");
                                }
                            }, 1500);
                        }

                    });

                }

                @Override
                public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                    super.onLoadMore(refreshLayout);
                    mTabModel.getListData(PageCode,mPage, 10, new BeanCallback<BaseJson<List<UserBean>>>() {
                        @Override
                        public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                            if (success){
                                if (response.getCode()==200 ){
                                    if (response.getData()!=null && response.getData().size()>0){
                                        mDataList.addAll(response.getData());
                                        mListAdapter.setDataList(mDataList);
                                        mPage++;
                                    }else {
                                        BaseUtil.makeText("没有更多数据");
                                        mTrlLoad.finishLoadmore();
                                        return;
                                    }

                                }
                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mTrlLoad.finishLoadmore();
                                    mListAdapter.notifyDataSetChanged();
                                    BaseUtil.makeText("加载完毕");
                                }
                            }, 1500);
                        }
                    });
                }
            });
        }

        mListAdapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                UserBean bean = (UserBean) holder.getData();
                IntentHelper.OpenPersonActivity(ListActivity.this,bean.getId());
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabModel.onDestroy();
        mPersonModel.onDestroy();
    }
}
