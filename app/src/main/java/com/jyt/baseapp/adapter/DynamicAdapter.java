package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.DynamicViewHolder;

/**
 * @author LinWei on 2018/5/15 11:00
 */
public class DynamicAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        DynamicViewHolder holder = new DynamicViewHolder(parent);
        holder.setOnImageClickListener(listener);
        return holder;
    }

    public DynamicViewHolder.OnImageClickListener listener;
    public void setOnImageClickListener(DynamicViewHolder.OnImageClickListener listener){
        this.listener = listener;
    }
}
