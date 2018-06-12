package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class BlackViewHolder extends BaseViewHolder<UserBean> {


    @BindView(R.id.iv_ff_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.tv_ff_state)
    TextView mTvState;
    @BindView(R.id.tv_ff_name)
    TextView mTvName;
    @BindView(R.id.tv_ff_mark)
    TextView mTvMark;


    public BlackViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(UserBean data) {
        super.setData(data);
        mTvState.setVisibility(View.GONE);
        mTvName.setText(data.getNickname());
        Glide.with(BaseUtil.getContext()).load(data.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
        mTvMark.setText(data.getIntroduction());
    }
}
