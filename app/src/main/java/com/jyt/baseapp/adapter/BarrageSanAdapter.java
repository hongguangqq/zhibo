package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BarrageSanViewHolder;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

/**
 * @author LinWei on 2018/7/17 19:33
 */
public class BarrageSanAdapter extends BaseRcvAdapter {

    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        BarrageSanViewHolder holder = new BarrageSanViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        return holder;
    }

}
