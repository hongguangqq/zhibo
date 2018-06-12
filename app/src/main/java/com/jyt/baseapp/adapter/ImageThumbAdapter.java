package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.ImageThumbViewHolder;

/**
 * Created by chenweiqi on 2017/1/9.
 */

public class ImageThumbAdapter extends BaseRcvAdapter {

    BaseViewHolder.OnViewHolderClickListener onCheckImageClickListener;
    BaseViewHolder.OnViewHolderClickListener onBrowseImageClickListener;



    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        ImageThumbViewHolder holder = new ImageThumbViewHolder(parent);
        holder.setOnBrowseImageClickListener(onBrowseImageClickListener);
        holder.setOnCheckImageClickListener(onCheckImageClickListener);
        return  holder;
    }

    public void setOnCheckImageClickListener(BaseViewHolder.OnViewHolderClickListener onCheckImageClickListener) {
        this.onCheckImageClickListener = onCheckImageClickListener;
    }

    public void setOnBrowseImageClickListener(BaseViewHolder.OnViewHolderClickListener onBrowseImageClickListener) {
        this.onBrowseImageClickListener = onBrowseImageClickListener;
    }


}
