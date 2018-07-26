package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.BarrageBean;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class BarrageSendViewHolder extends BaseViewHolder<BarrageBean> {


    @BindView(R.id.iv_holder_gift_send)
    ImageView mIvGiftSend;
    @BindView(R.id.tv_holder_gift_money)
    TextView mTvGiftMoney;
    @BindView(R.id.tv_holder_gift_item)
    TextView mTvGiftItem;

    public BarrageSendViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_barrage_gift, parent, false));
    }

    @Override
    public void setData(BarrageBean data) {
        super.setData(data);
        Glide.with(App.getContext()).load(data.getImg()).into(mIvGiftSend);
        mTvGiftMoney.setText(data.getPrice()+"币");
        mTvGiftItem.setText(data.getName());
    }


}
