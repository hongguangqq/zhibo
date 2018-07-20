package com.jyt.baseapp.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.TimeUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQualityStrategy;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatLiveCompositingLayout;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;
import java.util.UUID;

import static com.jyt.baseapp.App.getHandler;
import static com.jyt.baseapp.service.ScannerManager.trId;


public class LivePlayVoiceActivity extends BaseMCVActivity implements AVChatStateObserverLite {

    private TextView mTvTime;
    private Button mBtnHangUp;

    private LiveModel mLiveModel;
    private String mRoomName;
    private IPhoneDialog mExitDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_play_voice;
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
        if (AVChatManager.getInstance().isAudienceRole()){
            AVChatManager.getInstance().enableAudienceRole(false);
        }
        AVChatManager.getInstance().observeAVChatState(this, true);
        mTvTime = findViewById(R.id.tv_liveVoice_time);
        mBtnHangUp = findViewById(R.id.btn_liveVoice_hangup);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);
        mRoomName = UUID.randomUUID().toString();
    }

    private void initSetting(){
        AVChatManager.getInstance().enableRtc();
        AVChatManager.getInstance().enableVideo();
        if (true) {
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().muteLocalAudio(true);//开启音频
            AVChatManager.getInstance().setVideoQualityStrategy(AVChatVideoQualityStrategy.PreferFrameRate);
        }
        //parameters
        AVChatParameters parameters = new AVChatParameters();
        parameters.setBoolean(AVChatParameters.KEY_SESSION_LIVE_MODE, false);
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        parameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);

        parameters.set(AVChatParameters.KEY_SESSION_LIVE_COMPOSITING_LAYOUT, new AVChatLiveCompositingLayout(AVChatLiveCompositingLayout.Mode.LAYOUT_FLOATING_RIGHT_VERTICAL));
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_16_9);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, false);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        AVChatManager.getInstance().setParameters(parameters);


        AVChatManager.getInstance().createRoom(mRoomName, null, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {

                AVChatManager.getInstance().joinRoom2(mRoomName, AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
                    @Override
                    public void onSuccess(AVChatData avChatData) {
                        Log.e("@#","VoiceRoom进入成功--"+ mRoomName);
                        ScannerManager.starComTime = System.currentTimeMillis();//主播进入房间，记录开始时间
                        mLiveModel.AnchorAnswer(trId, mRoomName, Const.getWyAccount(), new BeanCallback() {
                            @Override
                            public void response(boolean success, Object response, int id) {

                            }
                        });
                    }

                    @Override
                    public void onFailed(int i) {
                        Log.e("@#","VoiceRoom进入失败--"+ i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Log.e("@#","VoiceRoom进入失败--"+ throwable);
                    }
                });
            }

            @Override
            public void onFailed(int i) {
                Log.e("@#","VoiceRoom创建失败--"+ i);
            }

            @Override
            public void onException(Throwable throwable) {

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

        mBtnHangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExitDialog.show();
            }
        });
    }


    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {
//        if (i == AVChatResCode.JoinChannelCode.OK ) {
//            AVChatManager.getInstance().setSpeaker(true);
//        }
    }

    @Override
    public void onUserJoined(String s) {
        if (!TextUtils.isEmpty(ScannerManager.comID) && ScannerManager.comID.equals(s)){
            AVChatManager.getInstance().setSpeaker(true);
            BaseUtil.makeText("观众已进入房间");
            mhandle.post(timeRunable);

        }
    }

    @Override
    public void onUserLeave(String s, int i) {
        if (!TextUtils.isEmpty(ScannerManager.comID) && ScannerManager.comID.equals(s)){
            releaseRtc(this,true,true);
        }
    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onProtocolIncompatible(int i) {

    }

    @Override
    public void onDisconnectServer(int i) {
        releaseRtc(this,true,true);
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

    //计时器
    private Handler mhandle = new Handler();
    private boolean isPause = false;//是否暂停
    private long currentSecond = 0;//当前毫秒数
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

    public  void releaseRtc(final Activity activity , boolean isReleaseRtc, boolean isLeaveRoom) {

        if (isLeaveRoom) {
            //挂断电话
//            mLiveModel.DoneHangUp(new BeanCallback() {
//                @Override
//                public void response(boolean success, Object response, int id) {
//
//                }
//            });
            //离开房间
           AVChatManager.getInstance().leaveRoom2(mRoomName, new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("@#", "leave channel success");

                }

                @Override
                public void onFailed(int i) {
                    Log.e("@#", "leave channel failed, code:" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    Log.e("@#", "leave channel exception, throwable:" + throwable.getMessage());
                }
            });
            ScannerManager.endComTome = System.currentTimeMillis();//记录退出时间
        }
        if (activity != null) {
            activity.finish();
            isPause = true;
            IntentHelper.OpenEndCallActivity(LivePlayVoiceActivity.this,true);
        }

    }

    private void doCompletelyFinish() {

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                releaseRtc(LivePlayVoiceActivity.this,true,true);

            }
        }, 50);
    }

    @Override
    public void onBackPressed() {
        mExitDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveModel.onDestroy();
        AVChatManager.getInstance().disableRtc();
        AVChatManager.getInstance().observeAVChatState(this, false);
    }
}
