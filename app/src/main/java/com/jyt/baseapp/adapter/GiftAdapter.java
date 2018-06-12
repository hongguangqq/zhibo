package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.GiftViewHolder;

/**
 * @author LinWei on 2018/5/8 11:11
 */
public class GiftAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        GiftViewHolder holder = new GiftViewHolder(parent);
        return holder;
    }
}
