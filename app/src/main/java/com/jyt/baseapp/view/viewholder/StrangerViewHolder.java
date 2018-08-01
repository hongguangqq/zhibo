package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.FriendNewsBean;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class StrangerViewHolder extends BaseViewHolder<FriendNewsBean> {

    @BindView(R.id.iv_ff_hpic)
    CircleImageView mIVHpic;
    @BindView(R.id.tv_ff_state)
    TextView mTvState;
    @BindView(R.id.tv_ff_name)
    TextView mTvName;
    @BindView(R.id.tv_ff_mark)
    TextView mTvMark;
    @BindView(R.id.btn_appoint_text)
    Button mBtnAppoint;
    @BindView(R.id.iv_ff_video)
    ImageView mIvVideo;



    public StrangerViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(final FriendNewsBean data) {
        super.setData(data);
        mTvState.setVisibility(View.GONE);
        mIvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.openVideo(data);
                }
            }
        });
        mBtnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.openCom(data);
                }
            }
        });
        Glide.with(App.getContext()).load(data.getHeadImg()).error(R.mipmap.timg).into(mIVHpic);
        mTvName.setText(data.getNickname());

        mTvMark.setText(data.getContent());
        if (Const.getGender()==1){
            mIvVideo.setVisibility(View.VISIBLE);
            mBtnAppoint.setVisibility(View.GONE);
        }else {
            mIvVideo.setVisibility(View.GONE);
            mBtnAppoint.setVisibility(View.VISIBLE);
            mBtnAppoint.setText("私信");
        }


    }

    public OnOpenVideoListener listener;
    public interface OnOpenVideoListener{
        void openVideo(FriendNewsBean user);
        void openCom(FriendNewsBean user);
    }

    public void setOnOpenVideoListener(OnOpenVideoListener listener){
        this.listener = listener;
    }
}
