package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jyt.baseapp.R;

import io.rong.imlib.model.Message;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class ComOtherViewHolder extends BaseViewHolder<Message> {


    public ComOtherViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_com_other, parent, false));
    }

    @Override
    public void setData(Message data) {
        super.setData(data);
    }
}
