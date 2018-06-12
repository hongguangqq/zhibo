package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;

import static com.jyt.baseapp.R.id.iv_ff_hpic;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class NewViewHolder3 extends BaseViewHolder<String> {


    @BindView(iv_ff_hpic)
    ImageView mIvHpic;
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

    public NewViewHolder3(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        mIvHpic.setImageResource(R.mipmap.icon_guanfangtouxiang);
        mTvState.setVisibility(View.GONE);
        mTvTime.setVisibility(View.VISIBLE);
    }
}
