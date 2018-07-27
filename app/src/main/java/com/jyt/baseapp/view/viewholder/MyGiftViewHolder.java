package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.PersonBean;

import butterknife.BindView;

/**
 * @author LinWei on 2018/7/27 17:02
 */
public class MyGiftViewHolder extends BaseViewHolder<PersonBean.GiftData> {

    @BindView(R.id.iv_holder_mygift_pic)
    ImageView mIvPic;
    @BindView(R.id.tv_holder_mygift_num)
    TextView mTvNum;

    public MyGiftViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_mygift, parent, false));
    }

    @Override
    public void setData(PersonBean.GiftData data) {
        super.setData(data);
        Glide.with(App.getContext()).load(data.getImage()).into(mIvPic);
        mTvNum.setText("X"+data.getTotalCount());
        mTvNum.setTypeface(App.getInstace().getTypeface());
    }
}
