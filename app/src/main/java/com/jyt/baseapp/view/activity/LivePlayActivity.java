package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.jyt.baseapp.App.getHandler;


public class LivePlayActivity extends BaseMCVActivity {
    private FrameLayout mFlLocalRender;
    private FrameLayout mFlRemoterRender;
    private ImageView mIvHorn;//音频开关
    private ImageView mIvCamera;//视频开关
    private ImageView mIvNarrow;//窗口模式开关
    private Button mBtnStar;
    private String mMeetingName;
    private IPhoneDialog mExitDialog;
    private Handler mVideoEffectHandler = new Handler();
    private boolean isMeLeave;//是否为自己主动发起离开




    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_play;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initStting();
        initListener();

    }

    private void init(){
        HideActionBar();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        EventBus.getDefault().register(this);
        mFlLocalRender = findViewById(R.id.fl_localRender);
        mFlRemoterRender = findViewById(R.id.fl_remoteRender);
        mBtnStar = findViewById(R.id.btn_star);
        mIvHorn = findViewById(R.id.iv_live_horn);
        mIvCamera = findViewById(R.id.iv_live_camera);
        mIvNarrow = findViewById(R.id.iv_live_narrow);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
        if (ScannerManager.isStartLive){
            mBtnStar.setVisibility(View.GONE);//隐藏直播开启按钮
        }
    }

    private void initStting(){
        ScannerManager.isBigScreen = true;//进入该界面默认为大屏显示
        if (!BaseUtil.isServiceRunning(this,"com.jyt.baseapp.service.ScannerService")){
            ScannerController.getInstance().startMonkServer(this);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ScannerController.getInstance().SwitchLive(true);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mFlRemoterRender.addView(ScannerController.getInstance().getLocalRender(),params);
                //                    ScannerController.getInstance().show();
            }
        },1000);


    }

    private void initListener(){
        mBtnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlRemoterRender.removeAllViews();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != ScannerController.getInstance().getLocalRender()){
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            //创建房间
                            if (!ScannerManager.isStartLive){
                                ScannerController.getInstance().createAndJoinRoom();
                            }
                            mFlLocalRender.addView(ScannerController.getInstance().getLocalRender(),params);
                            mFlRemoterRender.addView(ScannerController.getInstance().getRemoteRender(),params);
                            mBtnStar.setVisibility(View.GONE);//隐藏直播开启按钮
                        }

                    }
                }, 1000);
            }
        });
        mExitDialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                doCompletelyFinish();
            }

            @Override
            public void ClickCancel() {

            }
        });

        mIvHorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerController.getInstance().muteLocalAudio();
            }
        });

        mIvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerController.getInstance().muteLocalVideo();
            }
        });

        mIvNarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ScannerManager.isAudienceJoin){
                    ScannerController.getInstance().show();
                    finish();
                }else {
                    BaseUtil.makeText("当前无用户进入，不可使用该功能");
                }

            }
        });
    }



    // 退出聊天室
    private void logoutChatRoom() {
        if (mExitDialog.isShowing()){
            doCompletelyFinish();
        }else {
            mExitDialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventOver(EventBean bean) {
        if (Const.Event_Live.equals(bean.getCode())){
            if (isMeLeave){
                return;
            }
            finish();
            IntentHelper.OpenEndCallActivity(this,true);
        }else if (Const.Event_HangUp.equals(bean.getCode())){
            Log.e("@#","观众挂断");
            BaseUtil.makeText("观众已挂断");
            finish();
        }
    }

    private void doCompletelyFinish() {
        ScannerManager.isStartLive = false;
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isMeLeave = true;
                ScannerController.getInstance().closeScanner(LivePlayActivity.this,true,true);
                IntentHelper.OpenEndCallActivity(LivePlayActivity.this,true);
                finish();
            }
        }, 50);
    }

    @Override
    public void onBackPressed() {
        if (ScannerManager.isBigScreen){
            //未处于悬浮窗状态
            if (ScannerManager.isStartLive){
                logoutChatRoom();
            }else {
                ScannerController.getInstance().closeScanner(LivePlayActivity.this,true,false);
                finish();
            }

        }else {
            //处于悬浮窗状态
        }


    }



    @Override
    protected void onDestroy() {
        if (ScannerManager.isBigScreen){
            //未处于悬浮窗状态
            if (ScannerManager.isStartLive){
                ScannerController.getInstance().closeScanner(LivePlayActivity.this,true,true);
            }
            ScannerController.getInstance().stopMonkServer(this);
        }else {
            //处于悬浮窗状态

        }
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
