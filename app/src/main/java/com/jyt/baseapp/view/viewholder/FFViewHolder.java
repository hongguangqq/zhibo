package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class FFViewHolder extends BaseViewHolder<UserBean> {

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

    public FFViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(final UserBean data) {
        super.setData(data);
        mIvVideo.setVisibility(View.VISIBLE);
        mTvName.setText(data.getNickname());
        Glide.with(BaseUtil.getContext()).load(data.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
        mTvMark.setText(data.getIntroduction());
        mIvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.OnClick(data);
                }
            }
        });
        if (Const.getGender() ==1){
            mIvVideo.setVisibility(View.VISIBLE);
        }else {
            mIvVideo.setVisibility(View.INVISIBLE);
        }
        if (data.getOnlineState()==1){
            mTvState.setText("在线");
        }else {
            mTvState.setText("离线");
        }
    }


    private OnVideoClickListener mListener;
    public void setOnVideoClickListener(OnVideoClickListener listener){
        this.mListener = listener;
    }
    public interface OnVideoClickListener{
        void OnClick(UserBean data);
    }
}
