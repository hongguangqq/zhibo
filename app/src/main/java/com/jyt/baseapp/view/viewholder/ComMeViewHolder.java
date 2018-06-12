package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class ComMeViewHolder extends BaseViewHolder<Message> {


    @BindView(R.id.iv_me_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.tv_me_text)
    TextView mTvText;

    public ComMeViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_com_me, parent, false));
    }

    @Override
    public void setData(Message data) {
        super.setData(data);
        if ("RC:TxtMsg".equals(data.getObjectName())){
            TextMessage textMessage = (TextMessage) data.getContent();
            mTvText.setVisibility(View.VISIBLE);
            mTvText.setText(textMessage.getContent());
        }
    }
}
