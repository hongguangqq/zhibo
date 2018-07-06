package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;

import static com.jyt.baseapp.App.getHandler;


public class LivePlayActivity extends BaseMCVActivity {
    private FrameLayout mFlLocalRender;
    private FrameLayout mFlRemoterRender;
    private Button mBtnStar;
    private String mMeetingName;
    private IPhoneDialog mExitDialog;
    private Handler mVideoEffectHandler = new Handler();


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
        mFlLocalRender = findViewById(R.id.fl_localRender);
        mFlRemoterRender = findViewById(R.id.fl_remoteRender);
        mBtnStar = findViewById(R.id.btn_star);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
        if (ScannerManager.isStartLive){
            mBtnStar.setVisibility(View.GONE);//隐藏直播开启按钮
        }
    }

    private void initStting(){
        ScannerManager.isBigScreen = true;
        if (!BaseUtil.isServiceRunning(this,"com.jyt.baseapp.service.ScannerService")){
            ScannerController.getInstance().startMonkServer(this);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
    }



    // 退出聊天室
    private void logoutChatRoom() {
        if (mExitDialog.isShowing()){
            doCompletelyFinish();
        }else {
            mExitDialog.show();
        }
    }

    private void doCompletelyFinish() {
        ScannerManager.isStartLive = false;
        //        showLiveFinishLayout();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ScannerController.getInstance().closeScanner(LivePlayActivity.this,true,true);
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
                Log.e("@#","finish");
            }

        }else {
            //处于悬浮窗状态
            finish();
        }


    }



    @Override
    protected void onDestroy() {
        if (ScannerManager.isBigScreen){
            //未处于悬浮窗状态
            ScannerController.getInstance().stopMonkServer(this);
        }else {
            //处于悬浮窗状态

        }
        super.onDestroy();

    }
}
