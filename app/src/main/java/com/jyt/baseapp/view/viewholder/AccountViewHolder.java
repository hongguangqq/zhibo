package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.WalletBean;
import com.jyt.baseapp.util.BaseUtil;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class AccountViewHolder extends BaseViewHolder<WalletBean> {


    @BindView(R.id.iv_account_bi)
    ImageView mIvBi;
    @BindView(R.id.tv_account_time)
    TextView mTvTime;
    @BindView(R.id.tv_account_number)
    TextView mTvNumber;
    @BindView(R.id.tv_account_tp)
    TextView mTvTp;
    @BindView(R.id.tv_account_mark)
    TextView mTvMark;

    public AccountViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_account, parent, false));
    }

    @Override
    public void setData(WalletBean data) {
        super.setData(data);
        if (data.getTotalPrice()>0){
            Glide.with(BaseUtil.getContext()).load(R.mipmap.icon_bi).into(mIvBi);
            mTvNumber.setText("+"+data.getTotalPrice());
        }else {
            Glide.with(BaseUtil.getContext()).load(R.mipmap.icon_bihui).into(mIvBi);
            mTvNumber.setText(""+data.getTotalPrice());
        }
        if (data.getPrePrice()==0){
            mTvTp.setText("");
        }else {
            mTvTp.setText(""+data.getPrePrice());
        }
        if (data.getPrice()!=0){
            mTvTp.setText(""+data.getPrice());
        }
        switch (data.getType()){
            case 1:
                mTvMark.setText("充值记录");
                break;
            case 2:
                mTvMark.setText("提现记录");
                break;
            case 3:
                mTvMark.setText("礼物赠送记录");
                break;
            case 4:
                mTvMark.setText("视频记录");
                break;
            case 5:
                mTvMark.setText("语音记录");
                break;
            case 6:
                mTvMark.setText("偷听记录");
                break;
            case 7:
                mTvMark.setText("推荐奖励");
                break;
        }
        mTvTime.setText(data.getCreateTime());
    }
}
