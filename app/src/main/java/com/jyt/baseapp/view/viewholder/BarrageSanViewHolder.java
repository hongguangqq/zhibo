package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class BarrageSanViewHolder extends BaseViewHolder<String> {


    @BindView(R.id.tv_holder_barrage_san)
    TextView mTvSan;

    public BarrageSanViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_barrage_san, parent, false));
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        mTvSan.setText(data);
    }


}
