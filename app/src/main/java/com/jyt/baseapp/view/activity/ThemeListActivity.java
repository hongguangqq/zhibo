package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.ListAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.TabModel;
import com.jyt.baseapp.model.impl.TabModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;


public class ThemeListActivity extends BaseMCVActivity {

    @BindView(R.id.rv_themeList_content)
    RecyclerView mRvContent;
    @BindView(R.id.trl_themeList_load)
    TwinklingRefreshLayout mTrlLoad;

    private List<UserBean> mList;
    private TabModel mTabModel;
    private ListAdapter mListAdapter;
    private int mPage;
    private int mActivityId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_theme_list;
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
        setTextTitle("参与用户");
        setvActionBarColor(R.color.bg_content);
        setvMainBackground(R.mipmap.bg_entrance);
        Tuple tuple = IntentHelper.ThemeListActivityGetPara(getIntent());
        mActivityId = (int) tuple.getItem1();
        mTabModel = new TabModelImpl();
        mTabModel.onStart(this);
        mListAdapter = new ListAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRvContent.setAdapter(mListAdapter);
    }

    private void initSetting(){
        mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPage = 0;//重置页数
                mTabModel.getActivityUser(mActivityId, mPage, new BeanCallback<BaseJson<List<UserBean>>>(ThemeListActivity.this,true,null) {
                    @Override
                    public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                        if (success && response.getCode()==200){
                            mPage++;
                            mList = response.getData();
                            mListAdapter.notifyData(mList);
                            mTrlLoad.finishRefreshing();
                        }else {
                            BaseUtil.makeText("刷新失败");
                            mTrlLoad.finishRefreshing();
                        }
                    }
                });
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mTabModel.getActivityUser(mActivityId, mPage, new BeanCallback<BaseJson<List<UserBean>>>(ThemeListActivity.this,true,null) {
                    @Override
                    public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                        if (success && response.getCode()==200){
                            mPage++;
                            mList.addAll(response.getData());
                            mListAdapter.notifyDataSetChanged();
                            mTrlLoad.finishLoadmore();
                        }else {
                            BaseUtil.makeText("没有更多数据");
                            mTrlLoad.finishLoadmore();
                        }
                    }
                });
            }
        });

        mListAdapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                UserBean bean = (UserBean) holder.getData();
                IntentHelper.OpenPersonActivity(ThemeListActivity.this,bean.getId());
            }
        });

        mTrlLoad.startRefresh();

    }
}
