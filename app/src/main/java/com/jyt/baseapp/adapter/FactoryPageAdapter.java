package com.jyt.baseapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jyt.baseapp.view.fragment.BaseFragment;
import com.jyt.baseapp.view.fragment.FragmentFactory;

/**
 * @author LinWei on 2018/5/8 09:57
 */
public class FactoryPageAdapter extends FragmentViewPagerAdapter {
    private int mCount;


    public FactoryPageAdapter(FragmentManager fm , int count) {
        super(fm);
        mCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment= FragmentFactory.createFragment(position);
        return fragment;

    }

    @Override
    public int getCount() {
        return mCount;
    }


}
