package com.jyt.baseapp.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jyt.baseapp.R;


import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by chenweiqi on 2017/11/8.
 */

public class ImageLoader {
    static Context appContext;
    static ImageLoader imageLoader;
    public static void init(Context context){
        appContext = context;
        imageLoader = new ImageLoader();
    }

    public static ImageLoader getInstance(){
        return imageLoader;
    }

    public void load(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imageView);
    }
    public void loadRectangle(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).centerCrop().into(imageView);
    }
    public void loadSquare(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).thumbnail(Glide.with(imageView.getContext()).load(R.drawable.loading_gif)).centerCrop().into(imageView);
    }
    public void loadCircle(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).thumbnail(Glide.with(imageView.getContext()).load(R.drawable.loading_gif)).bitmapTransform(new CropCircleTransformation(imageView.getContext())).centerCrop().into(imageView);

    }
    public void loadHeader(ImageView imageView, String url){
        Context context = imageView.getContext();
//        AnimationDrawable animationDrawable  = (AnimationDrawable) context.getResources().getDrawable(R.drawable.frame_loading);
//        url = "http://pic62.nipic.com/file/20150322/19858325_125956421000_2.jpg";
        Glide.with(context).load(url).thumbnail(Glide.with(context).load(R.drawable.loading_gif)).bitmapTransform(new CropCircleTransformation(context)).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

//    public void loadWithRadiusBorder(ImageView imageView, String url, int borderRadius_px, int borderWidth_px , int borderColor){
//        Glide.with(imageView.getContext()).load(url).thumbnail(Glide.with(imageView.getContext()).load(R.drawable.loading_gif)).diskCacheStrategy(DiskCacheStrategy.NONE).transform(new ImageBorderTransformation(imageView.getContext(),borderRadius_px,borderWidth_px,borderColor)).into(imageView);
//    }
}
