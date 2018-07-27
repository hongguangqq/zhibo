package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.AppointBean;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class NewViewHolder5 extends BaseViewHolder<AppointBean> {


    @BindView(R.id.tv_ff_state)
    TextView mTvState;
    @BindView(R.id.tv_ff_name)
    TextView mTvName;
    @BindView(R.id.tv_ff_mark)
    TextView mTvMark;
    @BindView(R.id.iv_ff_video)
    ImageView mIvVideo;
    @BindView(R.id.btn_appoint_text)
    Button mBtnAppoint;
    @BindView(R.id.iv_ff_hpic)
    CircleImageView mIvHpic;


    public NewViewHolder5(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(final AppointBean data) {
        super.setData(data);
        mTvState.setVisibility(View.GONE);
        mBtnAppoint.setVisibility(View.VISIBLE);
        if (Const.getGender() == 1) {
            mBtnAppoint.setText("取消预约");
            UserBean femaleUser = data.getToId();
            Glide.with(BaseUtil.getContext()).load(femaleUser.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
            mTvName.setText(femaleUser.getNickname());
            mTvMark.setText("预约时长："+data.getLength()+"分钟");
            mBtnAppoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        listener.CancelOrder(data.getFromId().getId());
                    }
                }
            });
        } else {
            mBtnAppoint.setText("回拨");
            UserBean maleUser = data.getFromId();
            Glide.with(BaseUtil.getContext()).load(maleUser.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
            mTvName.setText(maleUser.getNickname());
            mTvMark.setText("预约时长："+data.getLength()+"分钟");
            mBtnAppoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        listener.CallBack(data.getFromId().getId(),data.getId());
                    }
                }
            });
        }
    }



    private OnAppointListener listener;
    public void setOnAppointListener(OnAppointListener listener){
        this.listener = listener;
    }

    public interface OnAppointListener{
        void CallBack(int userId,int subId);
        void CancelOrder(int id);
    }


}
