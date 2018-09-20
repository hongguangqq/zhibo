package com.jyt.baseapp.view.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.manager.LiveManager;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.FastBlurUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


public class AnswerActivity extends BaseMCVActivity {

    @BindView(R.id.iv_answer_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_answer_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.iv_answer_sex)
    ImageView mIvSex;
    @BindView(R.id.tv_answer_name)
    TextView mTvName;
    @BindView(R.id.btn_answer_yes)
    Button mBtnYes;
    @BindView(R.id.btn_answer_no)
    Button mBtnNo;

    private Vibrator vibrator;
    private long downTime;
    private boolean mIsVoice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_answer;
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

    private void init() {
        HideActionBar();
        setvMainBackground(R.mipmap.bg_entrance);
        EventBus.getDefault().register(this);
        Tuple tuple = IntentHelper.AnswerActivityGetPara(getIntent());
        String name = (String) tuple.getItem1();
        String hpic = (String) tuple.getItem2();
        mIsVoice = (boolean) tuple.getItem3();
        vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        long[] patter = {1000, 1000, 2000, 50};
        if (Const.getVideoVibrate()){
            vibrator.vibrate(patter, 0);
        }
        if (Const.getVideoSound()){

        }
        if (Const.getVideoToast()){
            createNotification("您有新的视频通话请求");
        }
        downTime = System.currentTimeMillis();
        mTvName.setText(name);
        Glide.with(this).load(hpic).error(R.mipmap.timg).into(mIvHpic);

    }

    private void initSetting() {
        Resources rs = getResources();
        Bitmap scaledBitmap = BitmapFactory.decodeResource(rs, R.mipmap.timg);
        Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 5);
        mIvBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvBg.setImageBitmap(blurBitmap);
    }

    @OnClick(R.id.btn_answer_yes)
    public void AnswerYes(){
        long nowTime = System.currentTimeMillis();
        if (nowTime-downTime>25*1000){
            BaseUtil.makeText("已超过有效时间");
            finish();
            return;
        }
        if (mIsVoice){
            IntentHelper.OpenLivePlayVoiceActivity(this);
        }else {
            IntentHelper.OpenLivePlayActivity(this);
        }
        finish();
    }

    @OnClick(R.id.btn_answer_no)
    public void AnswerNo(){
        LiveManager.liveRejectCall(ScannerManager.uId);
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventOver(EventBean bean) {
        if (Const.Event_HangUp.equals(bean.getCode())){
            finish();
        }
    }

    private void createNotification(String title){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("我是标题")
                //设置内容
                .setContentText("我是内容")
                //设置大图标
                //                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置小图标
                //                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        vibrator.cancel();
    }
}
