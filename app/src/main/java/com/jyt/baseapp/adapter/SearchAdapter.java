package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.SearchViewHolder;

/**
 * @author LinWei on 2018/5/21 17:28
 */
public class SearchAdapter extends BaseRcvAdapter{

    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        SearchViewHolder holder = new SearchViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        return holder;
    }


}
