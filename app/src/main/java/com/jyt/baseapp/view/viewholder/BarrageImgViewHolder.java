package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.view.widget.BarrageMessage;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class BarrageImgViewHolder extends BaseViewHolder<BarrageMessage> {


    @BindView(R.id.tv_holder_barrage_name)
    TextView mTvName;
    @BindView(R.id.iv_holder_barrage_gift)
    ImageView mIvGift;


    public BarrageImgViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_barrage_img, parent, false));
    }

    @Override
    public void setData(BarrageMessage data) {
        super.setData(data);
        mTvName.setText(data.getName()+"：送出");
        Glide.with(App.getContext()).load(data.getImg()).into(mIvGift);
    }


}
