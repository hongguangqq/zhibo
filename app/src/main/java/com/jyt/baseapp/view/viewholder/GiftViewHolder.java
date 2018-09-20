package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.GiftBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/8 11:08
 */
public class GiftViewHolder extends BaseViewHolder<GiftBean> {
    @BindView(R.id.tv_hgift_time)
    TextView mTvHgiftTime;
    @BindView(R.id.iv_hgift_send)
    CircleImageView mIvHgiftSend;
    @BindView(R.id.tv_hgift_send)
    TextView mTvHgiftSend;
    @BindView(R.id.tv_hgift_receiver)
    TextView mTvHgiftReceiver;
    @BindView(R.id.iv_hgift_receiver)
    CircleImageView mIvHgiftReceiver;

    public GiftViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_gift, parent, false));
    }

    @Override
    public void setData(final GiftBean data) {
        super.setData(data);
        mTvHgiftSend.setText(data.getFromNickName());
        mTvHgiftReceiver.setText(data.getNickName());
        Glide.with(App.getContext()).load(data.getFromImg()).error(R.mipmap.timg).into(mIvHgiftSend);
        Glide.with(App.getContext()).load(data.getHeadImg()).error(R.mipmap.timg).into(mIvHgiftReceiver);
        mIvHgiftSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.OpenPersonActivity(getContext(),data.getFromId());
            }
        });
        mIvHgiftReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.OpenPersonActivity(getContext(),Integer.valueOf(data.getUserId()));
            }
        });
    }
}
