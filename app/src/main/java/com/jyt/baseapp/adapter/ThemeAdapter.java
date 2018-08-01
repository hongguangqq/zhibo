package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.ThemeViewHolder;

/**
 * @author LinWei on 2018/5/17 11:12
 */
public class ThemeAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        ThemeViewHolder holder = new ThemeViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        holder.setOnClickJoinListener(listener);
        return holder;
    }

    private ThemeViewHolder.OnClickJoinListener listener;
    public void setOnClickJoinListener(ThemeViewHolder.OnClickJoinListener listener){
        this.listener = listener;
    }

}
