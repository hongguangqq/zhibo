package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class NewViewHolder2 extends BaseViewHolder<String> {


    @BindView(R.id.tv_ff_state)
    TextView mTvState;
    @BindView(R.id.tv_ff_name)
    TextView mTvName;
    @BindView(R.id.tv_ff_mark)
    TextView mTvMark;
    @BindView(R.id.iv_ff_video)
    ImageView mIvVideo;



    public NewViewHolder2(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        mTvState.setVisibility(View.GONE);
        mIvVideo.setVisibility(View.VISIBLE);
        mIvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.Open();
                }
            }
        });
    }

    public OnOpenVideoListener listener;
    public interface OnOpenVideoListener{
        void Open();
    }

    public void setOnOpenVideoListener(OnOpenVideoListener listener){
        this.listener = listener;
    }
}
