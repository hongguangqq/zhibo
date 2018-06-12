package com.jyt.baseapp.view.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.FastBlurUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.OnClick;


public class LaunchActivity extends BaseMCVActivity {

    @BindView(R.id.iv_launch_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_launch_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.iv_launch_sex)
    ImageView mIvSex;
    @BindView(R.id.tv_launch_name)
    TextView mTvName;
    @BindView(R.id.tv_launch_star)
    TextView mTvStar;
    @BindView(R.id.pb_launch_progress)
    ProgressBar mPbProgress;
    @BindView(R.id.btn_launch_star)
    Button mBtnStar;

    private boolean isLaunch;
    private ValueAnimator mValueAnimator;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initSetting();
    }

    private void init(){
        HideActionBar();
        setvMainBackground(R.mipmap.bg_entrance);
    }

    private void initSetting(){

        Resources rs = getResources();
        Bitmap scaledBitmap = BitmapFactory.decodeResource(rs, R.mipmap.timg);
        Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 5);
        mIvBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvBg.setImageBitmap(blurBitmap);
        mValueAnimator = ValueAnimator.ofInt(0,400);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPbProgress.setProgress((Integer) animation.getAnimatedValue());
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isLaunch=false;
                mBtnStar.setText("拨号");
                BaseUtil.makeText("无人接听");
                mPbProgress.setVisibility(View.GONE);
                mPbProgress.setProgress(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.setDuration(30000);

    }

    @OnClick(R.id.btn_launch_star)
    public void LaunchTel(){
        if (!isLaunch){
            isLaunch=true;
            mTvStar.setVisibility(View.VISIBLE);
            mPbProgress.setVisibility(View.VISIBLE);
            mBtnStar.setText("挂断");
            mValueAnimator.start();
        }
    }




    public Bitmap getBitmap(String url) throws IOException {

        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }


}
