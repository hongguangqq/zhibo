package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.MyGiftViewHolder;

/**
 * @author LinWei on 2018/5/17 11:12
 */
public class MyGiftAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        MyGiftViewHolder holder = new MyGiftViewHolder(parent);
        return holder;
    }

}
