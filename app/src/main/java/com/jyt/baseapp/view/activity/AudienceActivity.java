package com.jyt.baseapp.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatResCode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCaptureOrientation;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

import static com.jyt.baseapp.App.getHandler;


public class AudienceActivity extends BaseMCVActivity implements AVChatStateObserverLite {
    private static final String TAG = "@#";

    private AVChatCameraCapturer mVideoCapturer;
    private AVChatSurfaceViewRenderer videoRender;//主播画面
    private AVChatCameraCapturer mVideoBypassCapturer;
    private AVChatSurfaceViewRenderer bypassVideoRender;//观众画面
    private IPhoneDialog mExitDialog;

    // state
    private boolean isStartVoice = true;//声音是否开启
    private boolean isStartVideo = true;//画面是否开启
    private boolean isStartLive = false; // 推流是否开始

    private String roomCreator = "96c372c5d70978f1239aac722c75080d";//主播的accid
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
        JoinChannel();
        initSetting();


    }

    private void init(){
        HideActionBar();
        AVChatManager.getInstance().observeAVChatState(this,true);
        roomName = "26fd326e-d3c0-4d2f-a39e-9cf5b229d25a";
        videoRender = findViewById(R.id.video_render);
        bypassVideoRender = findViewById(R.id.video_local_render);
        videoRender.setZOrderMediaOverlay(false);
        bypassVideoRender.setZOrderMediaOverlay(true);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
    }


    private void JoinChannel(){
        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }
        AVChatParameters parameters = new AVChatParameters();
        parameters.setBoolean(AVChatParameters.KEY_SESSION_LIVE_MODE, true);
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_16_9);
        int videoOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? AVChatVideoCaptureOrientation.ORIENTATION_PORTRAIT : AVChatVideoCaptureOrientation.ORIENTATION_LANDSCAPE_RIGHT;
        parameters.setInteger(AVChatParameters.KEY_VIDEO_CAPTURE_ORIENTATION, videoOrientation);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, true);
        AVChatManager.getInstance().setParameters(parameters);
        AVChatManager.getInstance().enableVideo();
        AVChatManager.getInstance().startVideoPreview();
        AVChatManager.getInstance().joinRoom2(roomName, AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                Log.e(TAG , "join channel success");
                showOnMicView(Const.getWyAccount());
            }

            @Override
            public void onFailed(int i) {
                Log.e(TAG , "join channel failed, code:" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG , "join channel exception, throwable:" + throwable.getMessage());
            }
        });
    }

    private void initSetting(){
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

    //观众连线
    public void showOnMicView(String account){
        if (Const.getWyAccount().equals(account)){
            bypassVideoRender.setVisibility(View.VISIBLE);
            AVChatManager.getInstance().setupLocalVideoRender(bypassVideoRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
    }


    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {

//        AVChatManager.getInstance().setMicrophoneMute(true);
//        AVChatManager.getInstance().muteLocalAudio(false);
        if (i == AVChatResCode.JoinChannelCode.OK ) {
            Log.e(TAG,"onJoinedChannel");
            AVChatManager.getInstance().setSpeaker(true);
        }
    }

    @Override
    public void onUserJoined(String s) {
        Log.e(TAG,"onUserJoined"+" s="+s);
        AVChatManager.getInstance().setupRemoteVideoRender(roomCreator, videoRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
    }

    @Override
    public void onUserLeave(String s, int i) {
        AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        AVChatManager.getInstance().stopVideoPreview();
        AVChatManager.getInstance().disableVideo();
        AVChatManager.getInstance().leaveRoom2(roomName, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
        AVChatManager.getInstance().disableRtc();
        mVideoCapturer = null;
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
        AVChatManager.getInstance().enableAudienceRole(false);
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
        return true;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame avChatAudioFrame) {
        return true;
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

    private void releaseRtc(boolean isReleaseRtc, boolean isLeaveRoom){

        if (isReleaseRtc) {
            AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();

        }
        if (isLeaveRoom){
            AVChatManager.getInstance().leaveRoom2(roomName, new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("@#","leave channel success");
                }

                @Override
                public void onFailed(int i) {
                    Log.e("@#","leave channel failed, code:" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    Log.e("@#","leave channel exception, throwable:" + throwable.getMessage());
                }
            });
        }
        AVChatManager.getInstance().disableRtc();
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
        isStartLive = false;
        //        showLiveFinishLayout();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                releaseRtc(true, true);
                finish();
            }
        }, 50);
    }

    @Override
    public void onBackPressed() {
        logoutChatRoom();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AVChatManager.getInstance().observeAVChatState(this,false);
//        AVChatManager.getInstance().stopVideoPreview();
//        //关闭音视频引擎
//        AVChatManager.getInstance().disableRtc();
//        // 如果是视频通话，关闭视频模块
//        AVChatManager.getInstance().disableVideo();
//        //离开房间
//        AVChatManager.getInstance().leaveRoom2(roomName, new AVChatCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//            }
//
//            @Override
//            public void onFailed(int i) {
//
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//
//            }
//        });

    }


}
