package com.jyt.baseapp.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * @author LinWei on 2018/5/4 14:41
 */
public class FragmentTab3 extends BaseFragment {
    @BindView(R.id.tb_tab3_indicator)
    TabLayout mTbIndicator;
    @BindView(R.id.vp_tab3_list)
    ViewPager mVpList;
    Unbinder unbinder;

    private List<String> mTitlelist;
    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mTab3Adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab3;
    }

    @Override
    protected void firstInit() {
        init();
        initSetting();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void init(){
        mTitlelist = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        mTitlelist.add("礼物榜");
        mTitlelist.add("土豪榜");
        mTitlelist.add("女神榜");
        mFragmentList.add(new GiftFragment());
        mFragmentList.add(new ListFragment(3));
        mFragmentList.add(new ListFragment(1));
        mTab3Adapter = new FragmentViewPagerAdapter(getChildFragmentManager());
    }

    private void initSetting(){
        mTab3Adapter.setTitles(mTitlelist);
        mTab3Adapter.setFragments(mFragmentList);
        mVpList.setOffscreenPageLimit(3);
        if (mVpList.getAdapter()==null){
            mVpList.setAdapter(mTab3Adapter);
            mTbIndicator.setupWithViewPager(mVpList);
            mTbIndicator.setTabsFromPagerAdapter(mTab3Adapter);
        }
        setIndicator(getActivity(),mTbIndicator,25,25);
    }


    /**
     * 修改导航条大小
     * @param context
     * @param tabs
     * @param leftDip 左边距
     * @param rightDip 右边距
     */
    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }



}
