package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.AccountViewHolder;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

/**
 * @author LinWei on 2018/5/17 11:12
 */
public class AccountAdapter extends BaseRcvAdapter {
    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        AccountViewHolder holder = new AccountViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        return holder;
    }

}
