package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.util.BaseUtil;

import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/8 16:51
 */
public class ListViewHolder extends BaseViewHolder<UserBean> {
    @BindView(R.id.iv_focus_hpic)
    ImageView mIvHpic;
    @BindView(R.id.tv_focus_price)
    TextView mTvPrice;
    @BindView(R.id.tv_focus_mark)
    TextView mTvMark;
    @BindView(R.id.tv_focus_online)
    TextView mTvOnline;
    @BindView(R.id.tv_focus_nick)
    TextView mTvNick;
    @BindView(R.id.tv_focus_work)
    TextView mTvWork;
    @BindView(R.id.rl_hfocus_content)
    RelativeLayout mRlContent;

    public ListViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_list, parent, false));
    }

    @Override
    public void setData(UserBean data) {
        super.setData(data);
        ViewGroup.LayoutParams params = mRlContent.getLayoutParams();
        params.width = BaseUtil.getScannerWidth() / 2;
        if (getPosition() == 1) {
            params.height = BaseUtil.dip2px(168);
        } else {
            params.height = BaseUtil.dip2px(337);
        }
        mRlContent.setLayoutParams(params);

        List<String> Pimg = data.getImgsArray();
        if (Pimg==null || Pimg.size()==0){
            Glide.with(BaseUtil.getContext()).load(R.mipmap.timg).into(mIvHpic);
        }else {
            Glide.with(BaseUtil.getContext()).load(Pimg.get(0)).error(R.mipmap.timg).into(mIvHpic);
        }
        mTvNick.setText(data.getNickname());
        mTvMark.setText(data.getMark());
        mTvWork.setText(data.getProfession());
        if (data.getOnlineState()==0){
            mTvOnline.setText("离线");
        }else {
            mTvOnline.setText("在线");
        }
        mTvPrice.setText(data.getPrice()+"币/分钟");
    }
}
