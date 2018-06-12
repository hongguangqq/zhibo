package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.PersonBean;
import com.jyt.baseapp.util.BaseUtil;

import butterknife.BindView;

/**
 * @author LinWei on 2018/6/7 15:17
 */
public class ItemViewHolder extends BaseViewHolder<PersonBean.GiftData> {


    @BindView(R.id.iv_item_logo)
    ImageView mIvLogo;
    @BindView(R.id.tv_item_no)
    TextView mTvNo;

    public ItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item, parent, false));
    }

    @Override
    public void setData(PersonBean.GiftData data) {
        super.setData(data);
        Glide.with(BaseUtil.getContext()).load(data.getImage()).into(mIvLogo);
        mTvNo.setText(String.valueOf(data.getTotalCount()));
    }

}
