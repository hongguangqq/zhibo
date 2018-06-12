package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/14 17:04
 */
public class VisitorViewHolder extends BaseViewHolder<UserBean> {
    @BindView(R.id.iv_hvistor_hpic)
    CircleImageView mIvHpic;

    public VisitorViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_visitor, parent, false));
    }

    @Override
    public void setData(UserBean data) {
        super.setData(data);
        Glide.with(BaseUtil.getContext()).load(data.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
    }
}
