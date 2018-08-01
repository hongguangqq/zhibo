package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.ThemeBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/17 11:10
 */
public class ThemeViewHolder extends BaseViewHolder<ThemeBean.ThemeBeanDta> {


    @BindView(R.id.iv_theme_hpic)
    CircleImageView mIvThemeHpic;
    @BindView(R.id.tv_theme_name)
    TextView mTvThemeName;
    @BindView(R.id.tv_theme_mark)
    TextView mTvThemeMark;
    @BindView(R.id.tv_theme_join)
    TextView mTvJoin;
    @BindView(R.id.ll_theme_join)
    LinearLayout mLlThemeJoin;

    public ThemeViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_theme, parent, false));
    }

    @Override
    public void setData(final ThemeBean.ThemeBeanDta data) {
        super.setData(data);
        Glide.with(BaseUtil.getContext()).load(data.getImgs()).error(R.mipmap.icon_theme).into(mIvThemeHpic);
        mTvThemeName.setText(data.getTitle());
        mTvThemeMark.setText(data.getContent());
        if (Const.getMyActivityId()==data.getId()){
            mTvJoin.setText("已参加");
        }else {
            mTvJoin.setText("参加");
        }
        mTvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mJoinListener!=null){
                    mJoinListener.Join(data.getId());
                }
            }
        });
    }

    private OnClickJoinListener mJoinListener;
    public interface OnClickJoinListener{
        void Join(int activityId);
    }
    public void setOnClickJoinListener( OnClickJoinListener listener){
        this.mJoinListener = listener;
    }


}
