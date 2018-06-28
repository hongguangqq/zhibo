package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;

import static com.jyt.baseapp.App.getHandler;


public class AudienceActivity extends BaseMCVActivity {
    private static final String TAG = "@#";
    private FrameLayout mFlRemoterRender;
    private FrameLayout mFlLocalRender;
    private IPhoneDialog mExitDialog;

    // state
    private boolean isStartVoice = true;//声音是否开启
    private boolean isStartVideo = true;//画面是否开启
    private boolean isStartLive = false; // 推流是否开始

    private String comId = "96c372c5d70978f1239aac722c75080d";//主播的accid
    private String roomName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_audience;
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
        roomName = "83cc601b-f0de-4179-89d9-8695e94ca495";
        mFlLocalRender = findViewById(R.id.fl_LocalRender);
        mFlRemoterRender = findViewById(R.id.fl_RemoteRender);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
    }




    private void initSetting(){
        ScannerManager.isBigScreen = true;
        if (!BaseUtil.isServiceRunning(this,"com.jyt.baseapp.service.ScannerService")){
            ScannerController.getInstance().startMonkServer(this);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!ScannerManager.isStartLive){
                    ScannerController.getInstance().joinRoom(roomName,comId);
                }
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mFlLocalRender.addView(ScannerController.getInstance().getLocalRender(),params);
                mFlRemoterRender.addView(ScannerController.getInstance().getRemoteRender(),params);
            }
        },1000);
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
                ScannerController.getInstance().closeScanner(AudienceActivity.this,true,true);
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
                ScannerController.getInstance().closeScanner(AudienceActivity.this,true,false);
                Log.e("@#","finish");
            }
            finish();
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
