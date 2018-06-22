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

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
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

    private boolean isLaunch = true;
    private ValueAnimator mValueAnimator;
    private int id;
    private int type;
    private int trid;
    private LiveModel mLiveModel;


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
        Tuple tuple = IntentHelper.LaunchActivityGetPara(getIntent());
        mLiveModel = new LiveModelImpl();
        id = (int) tuple.getItem1();
        type = (int) tuple.getItem2();
        String nick = (String) tuple.getItem3();
        String hpic = (String) tuple.getItme4();
        mTvName.setText(nick);
        Glide.with(this).load(hpic).error(R.mipmap.timg).into(mIvHpic);
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
                mLiveModel.HangUp(id, trid, new BeanCallback() {
                    @Override
                    public void response(boolean success, Object response, int id) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mLiveModel.MakeCall(id, type, new BeanCallback<BaseJson>() {
            @Override
            public void response(boolean success, BaseJson response, int id) {
                if (success && response.getCode()==200){
                    mPbProgress.setVisibility(View.VISIBLE);
                    mValueAnimator.start();
                }else if (success && response.getCode()==500){
                    BaseUtil.makeText(response.getMessage());
                    isLaunch=false;
                }
            }
        });

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
