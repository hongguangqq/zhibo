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
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.util.TimeUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class NewViewHolder2 extends BaseViewHolder<UserBean> {

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



    public NewViewHolder2(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(final UserBean data) {
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
        String time = TimeUtil.getTimeSlot((System.currentTimeMillis()-data.getEndTime())/100000);
        if (data.getGender()==1){
            mTvMark.setText(time);
        }else {
            mTvMark.setText(data.getPrice()+"/分钟  "+time);
        }
        if (Const.getGender()==1){

            mBtnAppoint.setVisibility(View.GONE);
            if (data.getGender()!=1){
                mIvVideo.setVisibility(View.VISIBLE);
            }else {
                mIvVideo.setVisibility(View.GONE);
            }
        }else {
            mIvVideo.setVisibility(View.GONE);
            mBtnAppoint.setText("私信");
            mBtnAppoint.setVisibility(View.VISIBLE);
        }

    }

    public OnOpenVideoListener listener;
    public interface OnOpenVideoListener{
        void openVideo(UserBean user);
        void openCom(UserBean user);
    }

    public void setOnOpenVideoListener(OnOpenVideoListener listener){
        this.listener = listener;
    }
}
