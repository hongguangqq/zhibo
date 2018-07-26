package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.FriendNewsBean;
import com.jyt.baseapp.util.TimeUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class NewViewHolder1 extends BaseViewHolder<FriendNewsBean> {

    @BindView(R.id.iv_ff_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.tv_ff_state)
    TextView mTvState;
    @BindView(R.id.tv_ff_name)
    TextView mTvName;
    @BindView(R.id.tv_ff_mark)
    TextView mTvMark;
    @BindView(R.id.iv_ff_video)
    ImageView mIvVideo;
    @BindView(R.id.tv_new_text)
    TextView mTvTime;

    public NewViewHolder1(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(FriendNewsBean data) {
        super.setData(data);
        mTvState.setVisibility(View.GONE);
        mTvTime.setVisibility(View.VISIBLE);
        Glide.with(App.getContext()).load(data.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
        mTvName.setText(data.getNickname());
        mTvMark.setText(data.getContent());
        mTvTime.setText(TimeUtil.getTimeSlot((System.currentTimeMillis()-data.getTime())/100000));
    }


}
