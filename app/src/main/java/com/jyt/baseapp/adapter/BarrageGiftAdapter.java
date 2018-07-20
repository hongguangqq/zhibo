package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BarrageSendViewHolder;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

/**
 * @author LinWei on 2018/7/17 19:33
 */
public class BarrageGiftAdapter extends BaseRcvAdapter {

    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        BarrageSendViewHolder holder = new BarrageSendViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        return holder;
    }

}
