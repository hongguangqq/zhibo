package com.jyt.baseapp.view.viewholder;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class ComOtherViewHolder extends BaseViewHolder<Message> {


    @BindView(R.id.iv_other_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.tv_other_text)
    TextView mTvText;
    @BindView(R.id.tv_other_second)
    TextView mTvSecond;
    @BindView(R.id.ll_other_voice)
    LinearLayout mLlVoice;
    @BindView(R.id.iv_san)
    ImageView mIvSan;
    @BindView(R.id.iv_other_img)
    ImageView mIvImg;

    public ComOtherViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_com_other, parent, false));
    }

    @Override
    public void setData(Message data) {
        super.setData(data);
        if ("RC:TxtMsg".equals(data.getObjectName())) {
            TextMessage textMessage = (TextMessage) data.getContent();
            mIvSan.setVisibility(View.VISIBLE);
            mTvText.setVisibility(View.VISIBLE);
            mLlVoice.setVisibility(View.GONE);
            mIvImg.setVisibility(View.GONE);
            mTvText.setText(textMessage.getContent());
        } else if ("RC:VcMsg".equals(data.getObjectName())) {
            final VoiceMessage voiceMessage = (VoiceMessage) data.getContent();
            mIvSan.setVisibility(View.VISIBLE);
            mLlVoice.setVisibility(View.VISIBLE);
            mTvText.setVisibility(View.GONE);
            mIvImg.setVisibility(View.GONE);
            mTvSecond.setText(voiceMessage.getDuration() + "秒");
            mLlVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PlayListener != null) {
                        PlayListener.PlayVoice(voiceMessage.getUri());
                    }
                }
            });
        } else if ("RC:ImgMsg".equals(data.getObjectName())) {
            ImageMessage imageMessage = (ImageMessage) data.getContent();
            mIvSan.setVisibility(View.GONE);
            mIvImg.setVisibility(View.VISIBLE);
            mTvText.setVisibility(View.GONE);
            mLlVoice.setVisibility(View.GONE);
            Glide.with(BaseUtil.getContext()).load(imageMessage.getMediaUrl()).into(mIvImg);
        }
    }

    private OnVoicePlayListener PlayListener;

    public void setOnVoicePlayListener(OnVoicePlayListener listener) {
        this.PlayListener = listener;
    }

    public interface OnVoicePlayListener {
        void PlayVoice(Uri uri);
    }
}
