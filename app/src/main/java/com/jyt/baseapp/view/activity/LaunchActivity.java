package com.jyt.baseapp.view.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.manager.LiveManager;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.CallBean;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.FastBlurUtil;
import com.jyt.baseapp.util.FinishActivityManager;
import com.jyt.baseapp.view.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private int uid;
    private int type;
    private int trid;
    private LiveModel mLiveModel;
    private CallBean mCallBean;


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
        EventBus.getDefault().register(this);
//        MiPushClient.setAlias(this, Const.getUserID(),null);//设置别名
        mLiveModel = new LiveModelImpl();
        uid = (int) tuple.getItem1();//对方用户的ID
        type = (int) tuple.getItem2();
        String nick = (String) tuple.getItem3();
        String hpic = (String) tuple.getItme4();
        mTvName.setText(nick);
        Glide.with(this).load(hpic).error(R.mipmap.timg).into(mIvHpic);
    }

    private void initSetting(){
        //背景高斯模糊处理
        Resources rs = getResources();
        Bitmap scaledBitmap = BitmapFactory.decodeResource(rs, R.mipmap.timg);
        Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 5);
        mIvBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvBg.setImageBitmap(blurBitmap);
        mValueAnimator = ValueAnimator.ofInt(0,300);
        mValueAnimator.setDuration(30000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPbProgress.setProgress((Integer) animation.getAnimatedValue());
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //拨打主播，视频聊天

                mLiveModel.MakeCall(uid, type, new BeanCallback<BaseJson<CallBean>>() {
                    @Override
                    public void response(boolean success, BaseJson<CallBean> response, int id) {
                        if (success && response.getCode()==200){
                            mPbProgress.setVisibility(View.VISIBLE);
                            mCallBean = response.getData();
                            trid = mCallBean.getId();
                            ScannerManager.trId = String.valueOf(trid);
                            if (type==2){
                                LiveManager.requsetGreateRoom(String.valueOf(uid));
                            }else if (type==3){
                                LiveManager.requsetGreateVoiceRoom(String.valueOf(uid));
                            }
                        }else {
                            BaseUtil.makeText(response.getMessage());
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isLaunch=false;
                mBtnStar.setText("拨号");
                mPbProgress.setVisibility(View.GONE);
                mTvStar.setVisibility(View.GONE);
                mPbProgress.setProgress(0);
                //Activity被销毁，动画播放仍然存在，会导致30秒后用户端发起挂断。加上当前Activity是否存在的判断，防止该状况发生。
                if (FinishActivityManager.getManager().IsActivityExist(LaunchActivity.class)){
                    mLiveModel.HangUp(uid, trid, new BeanCallback() {
                        @Override
                        public void response(boolean success, Object response, int id) {
                            LiveManager.audienceHangUp(String.valueOf(uid));
                        }
                    });
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.start();


    }

    @OnClick(R.id.btn_launch_star)
    public void LaunchTel(){
        if (!isLaunch){
            isLaunch=true;
            mTvStar.setVisibility(View.VISIBLE);
            mPbProgress.setVisibility(View.VISIBLE);
            mBtnStar.setText("挂断");
            mValueAnimator.start();
        }else {
            isLaunch = false;
            mTvStar.setVisibility(View.GONE);
            mPbProgress.setVisibility(View.GONE);
            mBtnStar.setText("重拨");
            mLiveModel.HangUp(uid, trid, new BeanCallback() {
                @Override
                public void response(boolean success, Object response, int id) {
                    LiveManager.audienceHangUp(String.valueOf(uid));
                    finish();
                }
            });

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventOver(EventBean bean) {
       if (Const.Event_Launch.equals(bean.getCode())){
           Log.e("@#","销毁拨打电话界面");
           finish();
       }else if (Const.Event_LiveHangUp.equals(bean.getCode())){
           BaseUtil.makeText("主播忙碌");
           mLiveModel.HangUp(uid, trid, new BeanCallback() {
               @Override
               public void response(boolean success, Object response, int uid) {
                   finish();
               }
           });

       }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (isLaunch){
//            mLiveModel.HangUp(uid, trid, new BeanCallback() {
//                @Override
//                public void response(boolean success, Object response, int uid) {
//
//                }
//            });
        }
    }



}
