package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.TimeUtil;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class EavesdropActivity extends BaseMCVActivity implements AVChatStateObserverLite {

    @BindView(R.id.tv_eavesdrop_time)
    TextView mTvTime;
    @BindView(R.id.tv_eavesdrop_money)
    TextView mTvMoney;
    @BindView(R.id.tv_eavesdrop_num)
    TextView mTvNum;
    @BindView(R.id.btn_eavesdrop_close)
    Button mBtnClose;
    @BindView(R.id.btn_eavesdrop_switch)
    Button mBtnSwitch;

    private LiveModel mLiveModel;
    private UserBean mUserBean;
    //计时器
    private Handler mhandle = new Handler();
    private boolean isPause = false;//是否暂停
    private long currentSecond = 0;//当前毫秒数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eavesdrop;
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
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);

    }

    private void initSetting(){
        JoinEavesdropRoom();

    }

    private void JoinEavesdropRoom(){
        mLiveModel.EavesdropLive(new BeanCallback<BaseJson<UserBean>>() {
            @Override
            public void response(boolean success, BaseJson<UserBean> response, int id) {
                if (success && response.getCode()==200){
                    mUserBean = response.getData();
                    AVChatManager.getInstance().enableRtc();
                    AVChatManager.getInstance().joinRoom2(mUserBean.getRoomName(), AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
                        @Override
                        public void onSuccess(AVChatData avChatData) {
                            Log.e("@#" , "join channel success");
                            AVChatManager.getInstance().enableAudienceRole(true);
                            mhandle.post(timeRunable);
                        }

                        @Override
                        public void onFailed(int i) {
                            Log.e("@#" , "join channel failed, code:" + i);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            Log.e("@#" , "join channel exception, throwable:" + throwable.getMessage());
                        }
                    });
                }else {
                    BaseUtil.makeText("搜寻失败，请重试");
                }
            }
        });
    }



    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            currentSecond = currentSecond + 1000;
            mTvTime.setText(TimeUtil.getFormatHMS(currentSecond));
            if (!isPause) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandle.postDelayed(this, 1000);
            }
        }
    };

    @OnClick(R.id.btn_eavesdrop_close)
    public void CloseEavesDrop(){
        //挂断电话
        isPause=true;
    }




    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {

    }

    @Override
    public void onUserJoined(String s) {

    }

    @Override
    public void onUserLeave(String s, int i) {

    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onProtocolIncompatible(int i) {

    }

    @Override
    public void onDisconnectServer(int i) {

    }

    @Override
    public void onNetworkQuality(String s, int i, AVChatNetworkStats avChatNetworkStats) {

    }

    @Override
    public void onCallEstablished() {

    }

    @Override
    public void onDeviceEvent(int i, String s) {

    }

    @Override
    public void onConnectionTypeChanged(int i) {

    }

    @Override
    public void onFirstVideoFrameAvailable(String s) {

    }

    @Override
    public void onFirstVideoFrameRendered(String s) {

    }

    @Override
    public void onVideoFrameResolutionChanged(String s, int i, int i1, int i2) {

    }

    @Override
    public void onVideoFpsReported(String s, int i) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame avChatVideoFrame, boolean b) {
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame avChatAudioFrame) {
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int i) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> map, int i) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats avChatSessionStats) {

    }

    @Override
    public void onLiveEvent(int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveModel.onDestroy();
    }
}
