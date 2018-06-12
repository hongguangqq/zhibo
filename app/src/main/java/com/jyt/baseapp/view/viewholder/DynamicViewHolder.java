package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.DynamicBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;
import com.jyt.baseapp.view.widget.SlFlowLayout;

import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/14 17:04
 */
public class DynamicViewHolder extends BaseViewHolder<DynamicBean> {


    @BindView(R.id.iv_hdynamic_hpic)
    CircleImageView mIvpic;
    @BindView(R.id.tv_hdynamic_name)
    TextView mTvName;
    @BindView(R.id.tv_hdynamic_time)
    TextView mTvTime;
    @BindView(R.id.tv_hdynamic_content)
    TextView mTvContent;
    @BindView(R.id.flow_hdynamic_photo)
    SlFlowLayout mFlowPhoto;
    @BindView(R.id.iv_hdynamic_zang)
    ImageView mIvZang;
    @BindView(R.id.rl_hdynamic_videio)
    RelativeLayout mRlVideo;
    @BindView(R.id.iv_hdynamic_video)
    ImageView mIvVideo;

    private int width_img = BaseUtil.dip2px(95);
    private int height_img = BaseUtil.dip2px(95);
    private int space_img = (BaseUtil.getScannerWidth()-BaseUtil.dip2px(60)-width_img*3)/3;
    private boolean isZang;//是否点赞

    public DynamicViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_dynamic, parent, false));
    }

    @Override
    public void setData(final DynamicBean data) {
        super.setData(data);
        /*Test--------------------*/
        final Animation shake = AnimationUtils.loadAnimation(BaseUtil.getContext(), R.anim.shake_zang);
        mFlowPhoto.setChildSpacing(space_img);
        mFlowPhoto.setRowSpacing(space_img/2);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width_img,height_img);
        mTvName.setText(data.getUserId().getNickname());
        mTvTime.setText(data.getTime());
        if (data.getText()!=null){
            mTvContent.setText(data.getText());
        }
        if (data.getVideo()!=null){
            mRlVideo.setVisibility(View.VISIBLE);
            //视频地址
            mIvVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(BaseUtil.getContext()).load(data.getVedioImg()).error(R.mipmap.icon_vedio).into(mIvVideo);
            mRlVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.OpenVideo(data.getVideo());
                    }
                }
            });
        }else {
            mRlVideo.setVisibility(View.GONE);
        }
        Glide.with(BaseUtil.getContext()).load(data.getUserId().getHeadImg()).error(R.mipmap.timg).into(mIvpic);
        mFlowPhoto.removeAllViews();
        if (data.getImgs()!=null){
            final List<String> pathList = data.getImgsPath();
            for (int i = 0; i < pathList.size(); i++) {
                ImageView iv_photo = new ImageView(BaseUtil.getContext());
                iv_photo.setBackgroundColor(BaseUtil.getContext().getResources().getColor(R.color.white));
                Glide.with(BaseUtil.getContext()).load(pathList.get(i)).error(R.mipmap.timg).into(iv_photo);
                iv_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv_photo.setLayoutParams(params);
                final int finalI = i;
                iv_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            listener.OnClick(pathList, finalI);
                        }
                    }
                });
                mFlowPhoto.addView(iv_photo);
            }
        }
        mIvZang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvZang.startAnimation(shake);
                DianZang();
                if (listener!=null){
                    listener.OnZangClick(data.getId(),data.isFlag());
                }
            }
        });
        if (data.isFlag()){
            mIvZang.setImageDrawable(BaseUtil.getContext().getResources().getDrawable(R.mipmap.btn_xihuan_sel));
        }else {
            mIvZang.setImageDrawable(BaseUtil.getContext().getResources().getDrawable(R.mipmap.btn_xihuan));
        }

    }

    public void DianZang(){
        if (data.isFlag()){
            mIvZang.setImageDrawable(BaseUtil.getContext().getResources().getDrawable(R.mipmap.btn_xihuan));
            data.setFlag(false);
        }else {
            mIvZang.setImageDrawable(BaseUtil.getContext().getResources().getDrawable(R.mipmap.btn_xihuan_sel));
            data.setFlag(true);
        }
    }

    public interface OnImageClickListener{
        void OnClick(List<String> data,int index);
        void OnZangClick(int id , boolean flag);
        void OpenVideo(String path);
    }

    private OnImageClickListener listener;

    public void setOnImageClickListener(OnImageClickListener listener){
        this.listener = listener;
    }
}
