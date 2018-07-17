package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.BarrageBean;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class BarrageTextViewHolder extends BaseViewHolder<BarrageBean> {


    @BindView(R.id.tv_holder_barrage_name)
    TextView mTvName;
    @BindView(R.id.tv_holder_barrage_msg)
    TextView mTvMsg;

    public BarrageTextViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_barrage_text, parent, false));
    }

    @Override
    public void setData(BarrageBean data) {
        super.setData(data);
        mTvName.setText(data.getName());
        mTvMsg.setText(data.getText());
    }


}
