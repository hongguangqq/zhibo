package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.CallRecordBean;
import com.jyt.baseapp.util.TimeUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class NewViewHolder4 extends BaseViewHolder<CallRecordBean> {


    @BindView(R.id.iv_ff_hpic)
    CircleImageView mIVHpic;
    @BindView(R.id.tv_ff_state)
    TextView mTvState;
    @BindView(R.id.tv_ff_name)
    TextView mTvName;
    @BindView(R.id.tv_ff_mark)
    TextView mTvMark;
    @BindView(R.id.iv_ff_video)
    ImageView mIvVideo;
    @BindView(R.id.ll_con_content)
    LinearLayout mLlCon;
    @BindView(R.id.tv_con_text)
    TextView mTvCon;
    @BindView(R.id.iv_con)
    ImageView mIvCon;
    @BindView(R.id.tv_new_text)
    TextView mTvNext;


    public NewViewHolder4(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(CallRecordBean data) {
        super.setData(data);
        mIvVideo.setImageResource(R.mipmap.icon_guanfangtouxiang);
        mTvState.setVisibility(View.GONE);
        mLlCon.setVisibility(View.VISIBLE);
        mIvCon.setVisibility(View.VISIBLE);
        if (Const.getGender() == 1){
            //男性
            mTvName.setText(data.getToId().getNickname());
            Glide.with(App.getContext()).load(data.getToId().getHeadImg()).error(R.mipmap.timg).into(mIVHpic);
        }else {
            //女性
            mTvName.setText(data.getFromId().getNickname());
            Glide.with(App.getContext()).load(data.getFromId().getHeadImg()).error(R.mipmap.timg).into(mIVHpic);
        }
        if (data.getState()==1){
            if (data.isIn()){
                mIvCon.setImageResource(R.mipmap.icon_vin);
//                Glide.with(App.getContext()).load(R.mipmap.icon_vin).into(mIvCon);
                mTvCon.setText("音频呼入");
            }else {
                mIvCon.setImageResource(R.mipmap.icon_vout);
//                Glide.with(App.getContext()).load(R.mipmap.icon_vout).into(mIvCon);
                mTvCon.setText("音频呼出");
            }
        }else {
            if (data.isIn()){
                mIvCon.setImageResource(R.mipmap.icon_sin);
//                Glide.with(App.getContext()).load(R.mipmap.icon_sin).into(mIvCon);
                mTvCon.setText("音频呼入");
            }else {
                mIvCon.setImageResource(R.mipmap.icon_sout);
//                Glide.with(App.getContext()).load(R.mipmap.icon_sout).into(mIvCon);
                mTvCon.setText("音频呼出");
            }
        }
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long endTime = format.parse(data.getEndTime()).getTime();

            String time = TimeUtil.getTimeSlot((nowTime - endTime)/1000000);
            if (data.getState()==1){
                mTvMark.setText("通话时间 "+time);
            }else {
                mTvMark.setText("未接通 "+time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
