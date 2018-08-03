package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.ThemeAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.ThemeBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.SpacesItemDecoration;
import com.jyt.baseapp.model.TabModel;
import com.jyt.baseapp.model.impl.TabModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.ThemeViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jyt.baseapp.api.Const.MyJoinActivityId;


public class ThemeActivity extends BaseMCVActivity {

    @BindView(R.id.trl_theme_load)
    TwinklingRefreshLayout mTrlLoad;
    @BindView(R.id.rv_theme)
    RecyclerView mRvContent;


    private List<ThemeBean.ThemeBeanDta> mThemeList;
    private ThemeAdapter mAdapter;
    private TabModel mTabModel;
    private int mPage;
    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_theme;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initStting();
    }

    private void init(){
        setTextTitle("主题活动");
        setvMainBackground(R.mipmap.bg_entrance);
        mTabModel = new TabModelImpl();
        mTabModel.onStart(this);
        mThemeList = new ArrayList<>();
        mAdapter = new ThemeAdapter();

    }

    private void initStting(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvContent.addItemDecoration(new SpacesItemDecoration(0, BaseUtil.dip2px(10)));
        mRvContent.setAdapter(mAdapter);
        mTabModel.getThemeList(mPage, 5, new BeanCallback<BaseJson<ThemeBean>>() {
            @Override
            public void response(boolean success, BaseJson<ThemeBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        if (response.getData().getContent()!=null && response.getData().getContent().size()>0){
                            mThemeList = response.getData().getContent();
                            mAdapter.notifyData(mThemeList);
                        }
                    }
                }

            }
        });
        mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPage=0;
                mTabModel.getThemeList(mPage, 5, new BeanCallback<BaseJson<ThemeBean>>() {
                    @Override
                    public void response(boolean success, BaseJson<ThemeBean> response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                if (response.getData().getContent()!=null && response.getData().getContent().size()>0){
                                    mThemeList = response.getData().getContent();
                                }
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BaseUtil.makeText("刷新成功");
                                mTrlLoad.finishRefreshing();
                                mAdapter.notifyData(mThemeList);
                            }
                        }, 1500);
                    }
                });
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mTabModel.getThemeList(mPage, 5, new BeanCallback<BaseJson<ThemeBean>>() {
                    @Override
                    public void response(boolean success, BaseJson<ThemeBean> response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                if (response.getData().getContent()!=null && response.getData().getContent().size()>0){
                                    mThemeList.addAll(response.getData().getContent());
                                    mPage++;
                                }else {
                                    BaseUtil.makeText("没有更多活动数据");
                                }
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BaseUtil.makeText("加载成功");
                                mTrlLoad.finishLoadmore();
                                mAdapter.notifyData(mThemeList);
                            }
                        }, 1500);
                    }
                });
            }
        });

        mAdapter.setOnClickJoinListener(new ThemeViewHolder.OnClickJoinListener() {
            @Override
            public void Join(int activityId) {
                if (Const.getMyActivityId()!=activityId){
                    if (Const.getGender()==2){
                        mTabModel.joinActivity(activityId, new BeanCallback<BaseJson<Integer>>(ThemeActivity.this,true,"正在加入") {
                            @Override
                            public void response(boolean success, BaseJson<Integer> response, int id) {
                                if (success && response.getCode()==200){
                                    BaseUtil.setSpNumInt(MyJoinActivityId,response.getData());
                                    BaseUtil.makeText("加入成功");
                                    mAdapter.notifyDataSetChanged();
                                }else {
                                    BaseUtil.makeText("加入失败");
                                }
                            }
                        });
                    }else {
//                        BaseUtil.makeText("您已参加当前活动");
                    }
                }

            }
        });
        mAdapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                ThemeBean.ThemeBeanDta data = (ThemeBean.ThemeBeanDta) holder.getData();
                IntentHelper.OpenThemeListActivity(ThemeActivity.this,data.getId());
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabModel.onDestroy();
    }
}
