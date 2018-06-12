package com.jyt.baseapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author LinWei on 2018/5/4 15:48
 */
public class PageBannerAdapter extends PagerAdapter {

    private List<String> data;
    private Context mContext;

    public PageBannerAdapter(Context context , List<String> list){
        mContext = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position=position%data.size();
        String path=data.get(position);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(path).into(imageView);
        container.addView(imageView);
        return imageView;
    }

}
