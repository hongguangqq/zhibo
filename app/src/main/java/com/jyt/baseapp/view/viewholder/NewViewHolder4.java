package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class NewViewHolder4 extends BaseViewHolder<String> {


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


    public NewViewHolder4(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        mIvVideo.setImageResource(R.mipmap.icon_guanfangtouxiang);
        mTvState.setVisibility(View.GONE);
        mLlCon.setVisibility(View.VISIBLE);
    }
}
