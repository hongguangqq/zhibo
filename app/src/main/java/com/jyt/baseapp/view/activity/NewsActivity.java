package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.NewAdapter;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
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


    private NewAdapter mAdapter;
    private List<String> mDataList;

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
        switch (mCode){
            case 1:
                setTextTitle("好友消息");
                break;
            case 2:
                setTextTitle("谁看过我");
                break;
            case 3:
                setTextTitle("系统通知");
                break;
            case 4:
                setTextTitle("通话记录");
                break;
            case 5:
                setTextTitle("我的预约");
                break;

        }
        setvMainBackgroundColor(R.color.bg_content);
        mAdapter = new NewAdapter(mCode);
        mDataList = new ArrayList<>();
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
    }

    private void initSetting() {
        mTrlLoad.setEnableLoadmore(false);//不可上拉加载更多
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRvContent.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.line_color2)));
        mAdapter.setDataList(mDataList);
        mRvContent.setAdapter(mAdapter);
    }
}
