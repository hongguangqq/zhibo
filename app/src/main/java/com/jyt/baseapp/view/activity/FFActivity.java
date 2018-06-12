package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.fragment.FfFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FFActivity extends BaseMCVActivity {

    @BindView(R.id.tb_ff_indicator)
    TabLayout mTbIndicator;
    @BindView(R.id.vp_ff)
    ViewPager mVp;

    private List<String> mIndicatorList;
    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mAdapter;
    private int mPage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ff;
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
        setTextTitle("XX列表");
        setvMainBackgroundColor(R.color.bg_content);
        Tuple tuple = IntentHelper.FFActivityGetPara(getIntent());
        mPage = (int) tuple.getItem1();
        mIndicatorList = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        mIndicatorList.add("我的关注");
        mIndicatorList.add("关注我的");
        mFragmentList.add(new FfFragment(0));
        mFragmentList.add(new FfFragment(1));
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());

    }

    private void initSetting() {
        mAdapter.setTitles(mIndicatorList);
        mAdapter.setFragments(mFragmentList);
        mVp.setAdapter(mAdapter);
        mTbIndicator.setupWithViewPager(mVp);
        mTbIndicator.setTabsFromPagerAdapter(mAdapter);
        BaseUtil.setIndicator(this,mTbIndicator,25,25);
        mVp.setCurrentItem(mPage);
    }


}
