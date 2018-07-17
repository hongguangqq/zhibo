package com.jyt.baseapp.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.jyt.baseapp.App.getHandler;


public class AudienceActivity extends BaseMCVActivity {
    private static final String TAG = "@#";
    private FrameLayout mFlRemoterRender;
    private FrameLayout mFlLocalRender;
    private IPhoneDialog mExitDialog;

    private LiveModel mLiveModel;
    private boolean isMeLeave;//是否为自己主动发起离开
    private OptionsPickerView mBarragePickerView;
    private List mBarrageList;//弹幕列表


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
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        mFlLocalRender = findViewById(R.id.fl_LocalRender);
        mFlRemoterRender = findViewById(R.id.fl_RemoteRender);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);
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
                    ScannerController.getInstance().joinRoom(ScannerManager.mMeetingName,ScannerManager.comID);
                }
                ScannerController.getInstance().SwitchLive(false);
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
        mBarragePickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

            }
        }).setSubmitText("发送")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("消息")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventOver(EventBean bean) {
        if (Const.Event_Audience.equals(bean.getCode())){
            if (isMeLeave){
                return;
            }
            Log.e("@#","主播离开房间，直播结束");
            IntentHelper.OpenEndCallActivity(this,false);
            finish();
        }
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
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isMeLeave = true;
                ScannerController.getInstance().closeScanner(AudienceActivity.this,true,true);
                IntentHelper.OpenEndCallActivity(AudienceActivity.this,false);
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
                ScannerController.getInstance().closeScanner(AudienceActivity.this,true,false);
                finish();
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
            if (ScannerManager.isStartLive){

            }
        }else {
            //处于悬浮窗状态

        }
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mLiveModel.onDestroy();

    }




}
