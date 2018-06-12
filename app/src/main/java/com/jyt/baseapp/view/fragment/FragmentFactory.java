package com.jyt.baseapp.view.fragment;

import java.util.HashMap;

/**
 * @author LinWei on 2018/5/4 11:17
 */
public class FragmentFactory {
    // 保存Fragment集合,方便复用
    private static HashMap<Integer, BaseFragment> mFragmentMap = new HashMap<Integer, BaseFragment>();

    public static BaseFragment createFragment(int pos) {
        BaseFragment fragment = mFragmentMap.get(pos);
        //为空，则是第一次调用，需要新建后再加入map
        if (fragment==null){
            switch (pos){
                case 0:
                    fragment = new FragmentTab1();
                    break;
                case 1:
                    fragment = new FragmentTab2();
                    break;
                case 2:
                    fragment = new FragmentTab3();
                    break;
                case 3:
                    fragment = new FragmentTab4();
                    break;
            }
            mFragmentMap.put(pos, fragment);// 将fragment保存在集合中
        }

        return fragment;
    }
}
