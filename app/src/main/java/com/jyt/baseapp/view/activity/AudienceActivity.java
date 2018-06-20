package com.jyt.baseapp.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jyt.baseapp.R;
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

import org.yczbj.ycvideoplayerlib.VideoPlayer;

import java.util.Map;


public class AudienceActivity extends AppCompatActivity implements AVChatStateObserverLite {
    private static final String TAG = "@#";

    private AVChatSurfaceViewRenderer videoRender;

    // 播放器
    private VideoPlayer videoPlayer;
    // state
    private boolean isStartLive = false; // 推流是否开始
    private boolean isMyVideoLink = true; // 观众连麦模式，默认视频
    private boolean isMyAlreadyApply = false; // 我是否已经申请连麦
    private boolean isAgreeToLink = false; // 主播是否同意我连麦（为了确保权限时使用）

    private String roomCreator = "96c372c5d70978f1239aac722c75080d";
    private String roomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience);
        AVChatManager.getInstance().observeAVChatState(this,true);
        videoRender = findViewById(R.id.video_render);
        videoRender.setZOrderMediaOverlay(false);
        roomName = "95e08be0-c774-4477-9cbf-724d8a4b731d";
        JoinChannel();
        AVChatManager.getInstance().joinRoom2(roomName, AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                Log.e(TAG , "join channel success");
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


    private AVChatCameraCapturer mVideoCapturer;
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
    }


    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {
        Log.e(TAG,"onJoinedChannel");
        if (i == AVChatResCode.JoinChannelCode.OK ) {
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
        AVChatManager.getInstance().observeAVChatState(this,false);
    }
}
