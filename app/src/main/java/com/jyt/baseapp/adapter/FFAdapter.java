package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.FFViewHolder;

/**
 * @author LinWei on 2018/5/17 11:12
 */
public class FFAdapter extends BaseRcvAdapter {

    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        FFViewHolder holder = new FFViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        holder.setOnVideoClickListener(listener);
        return holder;
    }

    private FFViewHolder.OnVideoClickListener listener;
    public void setOnVideoClickListener(FFViewHolder.OnVideoClickListener listener){
        this.listener = listener;
    }

}
