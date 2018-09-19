package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.VisitorViewHolder;

/**
 * @author LinWei on 2018/5/14 17:07
 */
public class VisitorAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        VisitorViewHolder holder = new VisitorViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        return holder;
    }
}
