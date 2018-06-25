package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.view.dialog.IPhoneDialog;

import java.util.UUID;

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
        mFlLocalRender = findViewById(R.id.fl_localRender);
        mFlRemoterRender = findViewById(R.id.fl_remoteRender);
        mBtnStar = findViewById(R.id.btn_star);

        mMeetingName = UUID.randomUUID().toString();
        Log.e("@#","room--"+ mMeetingName);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
    }

    private void initStting(){
        ScannerController.getInstance().startMonkServer(LivePlayActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mFlLocalRender.addView(ScannerController.getInstance().getLocalRender(),params);
            }
        },1000);
    }

    private void initListener(){
        mBtnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != ScannerController.getInstance().getLocalRender()){


//                            mll.addView(ScannerController.getInstance().getLocalRender());
//                            ScannerController.getInstance().createAndJoinRoom();
//                            ScannerController.getInstance().show();
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
                ScannerController.getInstance().closeScanner(true,true);
                finish();
            }
        }, 50);
    }

    @Override
    public void onBackPressed() {
        if (ScannerManager.isBigScreen){
            if (ScannerManager.isStartLive){
                logoutChatRoom();
            }else {
                ScannerController.getInstance().closeScanner(true,false);
                finish();
            }
        }

    }



    @Override
    protected void onDestroy() {
        if (ScannerManager.isBigScreen){
            ScannerController.getInstance().closeConnection();
        }else {

        }
        super.onDestroy();

    }
}
