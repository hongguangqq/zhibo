package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.BlackViewHolder;

/**
 * @author LinWei on 2018/5/17 11:12
 */
public class BlackAdapter extends BaseRcvAdapter {

    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = new BlackViewHolder(parent);
        holder.setOnViewHolderLongClickListener(onViewHolderLongClickListener);
        return holder;
    }

}
