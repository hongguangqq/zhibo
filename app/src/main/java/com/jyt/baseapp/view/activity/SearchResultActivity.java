package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.SearchAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.SearchBean;
import com.jyt.baseapp.bean.SearchConditionBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
import com.jyt.baseapp.model.SearchModel;
import com.jyt.baseapp.model.impl.SearchModelImpl;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class SearchResultActivity extends BaseMCVActivity {

    @BindView(R.id.rv_list_content)
    RecyclerView mRvContent;
    @BindView(R.id.trl_list_load)
    TwinklingRefreshLayout mTrlLoad;
    private SearchBean mSearchData;
    private SearchConditionBean mCondition;
    private SearchAdapter mSearchAdapter;
    private int mPage = 1;
    private SearchModel mSearchModel;
    private List<UserBean> mSearchResult;


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
        setTextTitle("搜索结果");
        setvActionBarColor(R.color.bg_content);
        Tuple tuple = IntentHelper.SearchResultGetPara(getIntent());
        mSearchData = (SearchBean) tuple.getItem1();
        mCondition = (SearchConditionBean) tuple.getItem2();
        mSearchResult = mSearchData.getContent();
        mCondition.setPage(mPage);
        mSearchModel = new SearchModelImpl();
        mSearchAdapter = new SearchAdapter();
        mSearchAdapter.setDataList(mSearchResult);
        mTrlLoad.setEnableRefresh(false);
    }

    private void initSetting(){
        mRvContent.setAdapter(mSearchAdapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvContent.addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.line_color2)));
        mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mSearchModel.SearchByKey(mCondition, new BeanCallback<BaseJson<SearchBean>>() {
                    @Override
                    public void response(boolean success, BaseJson<SearchBean> response, int id) {
                        if (success) {
                            if (response.getCode() == 200) {
                                if (response.getData().getContent()!=null && response.getData().getContent().size()>0){
                                    mCondition.setPage(mPage++);
                                    mSearchResult.addAll(response.getData().getContent());
                                    mSearchAdapter.notifyDataSetChanged();

                                }
                            }
                        }
                        mTrlLoad.finishLoadmore();
                    }
                });

            }
        });
        mSearchAdapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                UserBean user = (UserBean) holder.getData();
                IntentHelper.OpenPersonActivity(SearchResultActivity.this,user.getId());
            }
        });

    }


}
