package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
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
    @BindView(R.id.ll_theme_join)
    LinearLayout mLlThemeJoin;

    public ThemeViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_theme, parent, false));
    }

    @Override
    public void setData(ThemeBean.ThemeBeanDta data) {
        super.setData(data);
        Glide.with(BaseUtil.getContext()).load(data.getImgs()).error(R.mipmap.icon_theme).into(mIvThemeHpic);
        mTvThemeName.setText(data.getTitle());
        mTvThemeMark.setText(data.getContent());
    }
}
