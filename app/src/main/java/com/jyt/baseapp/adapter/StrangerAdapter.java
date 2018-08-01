package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.StrangerViewHolder;

/**
 * @author LinWei on 2018/8/1 15:38
 */
public class StrangerAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        StrangerViewHolder holder = new StrangerViewHolder(parent);
        holder.setOnOpenVideoListener(mOpenVideoListener);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        return holder;
    }


    private StrangerViewHolder.OnOpenVideoListener mOpenVideoListener;
    public void setsetOnOpenVideoListener(StrangerViewHolder.OnOpenVideoListener listener){
        this.mOpenVideoListener = listener;
    }
}
