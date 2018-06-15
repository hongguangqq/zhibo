package com.jyt.baseapp.view.viewholder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.util.BaseUtil;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class SearchViewHolder extends BaseViewHolder<UserBean> {


    @BindView(R.id.tv_ff_state)
    TextView mTvState;
    @BindView(R.id.tv_ff_name)
    TextView mTvName;
    @BindView(R.id.tv_ff_mark)
    TextView mTvMark;
    @BindView(R.id.iv_ff_hpic)
    ImageView mIvHpic;


    public SearchViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_ff, parent, false));
    }

    @Override
    public void setData(UserBean data) {
        super.setData(data);
        mTvName.setText(data.getNickname());
        Glide.with(BaseUtil.getContext()).load(data.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
        if (!TextUtils.isEmpty(data.getIntroduction())){
            mTvMark.setText(data.getIntroduction());
        }else {
            mTvMark.setText("");
        }
        if (data.getOnlineState()==1){
            mTvState.setText("在线");
        }else {
            mTvState.setText("离线");
        }
    }


}
