package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.ItemViewHolder;

/**
 * @author LinWei on 2018/6/7 15:23
 */
public class ItemAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(parent);
        return holder;
    }
}
