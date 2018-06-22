package com.jyt.baseapp.view.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jyt.baseapp.R;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatDeviceEvent;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCaptureOrientation;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoFrameRate;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQuality;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatLiveCompositingLayout;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nrtc.sdk.common.ImageFormat;
import com.netease.vcloud.video.effect.VideoEffect;
import com.netease.vcloud.video.effect.VideoEffectFactory;

import java.util.Map;
import java.util.UUID;

import static com.jyt.baseapp.App.getHandler;


public class LivePlayActivity extends BaseMCVActivity implements AVChatStateObserverLite {
    private AVChatCameraCapturer mVideoCapturer;
    private AVChatSurfaceViewRenderer mRenderer;//主播画面
    private AVChatCameraCapturer mVideoBypassCapturer;
    private AVChatSurfaceViewRenderer bypassVideoRender;//观众画面
    private Button mBtnStar;
    private String mMeetingName;
    private IPhoneDialog mExitDialog;
    private boolean isStartLive = false; // 是否开始直播推流
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
        mRenderer = findViewById(R.id.video_render);
        bypassVideoRender = findViewById(R.id.video_local_render);
        mBtnStar = findViewById(R.id.btn_star);
        mRenderer.setZOrderMediaOverlay(false);
        bypassVideoRender.setZOrderMediaOverlay(true);
        mMeetingName = UUID.randomUUID().toString();
        Log.e("@#","room--"+ mMeetingName);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
    }

    private void initStting(){
        AVChatManager.getInstance().observeAVChatState(this,true);
        startPreview();

    }

    private void initListener(){
        mBtnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVChatManager.getInstance().createRoom(mMeetingName, null, new AVChatCallback<AVChatChannelInfo>() {
                    @Override
                    public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                        Log.e("@#","room创建成功--"+ mMeetingName);
                        AVChatManager.getInstance().joinRoom2(mMeetingName, AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
                            @Override
                            public void onSuccess(AVChatData avChatData) {
                                Log.e("@#", "Live join channel success");
                                isStartLive = true;//创建房间成功，推流开始
                                mBtnStar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });

                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == ResponseCode.RES_EEXIST){
                            Log.e("@#","房间已存在");
                        }else {
                            Log.e("@#","create room failed, code:" + code);
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Log.e("@#", "create room onException, throwable:" + exception.getMessage());
                    }
                });
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


    //主播直播前预览
    private void startPreview() {
        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
            mVideoCapturer.setFlash(true);//设置闪光灯
        }
        if (true){
            AVChatManager.getInstance().enableVideo();
        }
        //设置视频采集模块
        AVChatCameraCapturer videoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
        AVChatManager.getInstance().setupVideoCapturer(videoCapturer);
        if (true) {
            AVChatManager.getInstance().setupLocalVideoRender(mRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        //parameters
        AVChatParameters parameters = new AVChatParameters();
        parameters.setBoolean(AVChatParameters.KEY_SESSION_LIVE_MODE, true);
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        parameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
        parameters.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, AVChatVideoQuality.QUALITY_720P);
        //如果用到美颜功能，建议这里设为15帧
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_RATE, AVChatVideoFrameRate.FRAME_RATE_15);
        //如果不用美颜功能，这里可以设为25帧
        //parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_RATE, AVChatVideoFrameRate.FRAME_RATE_25);
        parameters.set(AVChatParameters.KEY_SESSION_LIVE_COMPOSITING_LAYOUT, new AVChatLiveCompositingLayout(AVChatLiveCompositingLayout.Mode.LAYOUT_FLOATING_RIGHT_VERTICAL));
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_16_9);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, true);
        int videoOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? AVChatVideoCaptureOrientation.ORIENTATION_PORTRAIT : AVChatVideoCaptureOrientation.ORIENTATION_LANDSCAPE_RIGHT;
        parameters.setInteger(AVChatParameters.KEY_VIDEO_CAPTURE_ORIENTATION, videoOrientation);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        AVChatManager.getInstance().setParameters(parameters);

        if (true){
            AVChatManager.getInstance().startVideoPreview();
        }
    }






    private void registerObservers(boolean register){
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(customNotification, register);
    }

    Observer<CustomNotification> customNotification = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification customNotification) {
            if (customNotification == null) {
                return;
            }
        }
    };


    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {

    }

    @Override
    public void onUserJoined(String s) {
        Log.e("@#","onUserJoined"+" s="+s);
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
        releaseRtc(true, true);
        finish();
    }

    @Override
    public void onNetworkQuality(String s, int i, AVChatNetworkStats avChatNetworkStats) {

    }

    @Override
    public void onCallEstablished() {

    }

    @Override
    public void onDeviceEvent(int i, String s) {
        if (i == AVChatDeviceEvent.VIDEO_CAMERA_SWITCH_OK) {

        }
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

    private VideoEffect mVideoEffect;
    private boolean mHasSetFilterType = false;
    private int mCurWidth, mCurHeight;
    private Bitmap mWaterMaskBitmapStatic;
    private Bitmap[] mWaterMaskBitmapDynamic;
    private boolean isUninitVideoEffect = false;// 是否销毁滤镜模块
    private boolean mIsmWaterMaskAdded = false;
    private int rotation;
    private int mDropFramesWhenConfigChanged = 0; //丢帧数
    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        if (frame == null || (Build.VERSION.SDK_INT < 18)) {
            return true;
        }
        if (mVideoEffect == null){
            mVideoEffect = VideoEffectFactory.getVCloudEffect();
            mVideoEffect.init(this, true, false);
            //需要delay 否则filter设置不成功
            mVideoEffect.setBeautyLevel(5);
            mVideoEffect.setFilterLevel(0.5f);
        }
        //分辨率、清晰度变化后设置丢帧数为2
        if (mCurWidth != frame.width || mCurHeight != frame.height) {
            mCurWidth = frame.width;
            mCurHeight = frame.height;
        }
        if (mVideoEffect == null) {
            return true;
        }
        mVideoEffect.addWaterMark(null, 0, 0);
        mVideoEffect.closeDynamicWaterMark(true);

        VideoEffect.DataFormat format = frame.format == ImageFormat.I420 ? VideoEffect.DataFormat.YUV420 : VideoEffect.DataFormat.NV21;


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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AVChatParameters parameters = new AVChatParameters();
        int videoOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? AVChatVideoCaptureOrientation.ORIENTATION_PORTRAIT : AVChatVideoCaptureOrientation.ORIENTATION_LANDSCAPE_RIGHT;
        parameters.setInteger(AVChatParameters.KEY_VIDEO_CAPTURE_ORIENTATION, videoOrientation);
        AVChatManager.getInstance().setParameters(parameters);
        AVChatManager.getInstance().setupLocalVideoRender(mRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);

    }

    private void releaseRtc(boolean isReleaseRtc, boolean isLeaveRoom){
        if (mVideoEffect != null) {
            mVideoEffectHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("@#", "releaseRtc unInit");
                    mVideoEffect.unInit();
                    mVideoEffect = null;
                }
            });
        }
        if (isReleaseRtc) {
            AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();

        }
        if (isLeaveRoom){
            AVChatManager.getInstance().leaveRoom2(mMeetingName, new AVChatCallback<Void>() {
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

    private void clearChatRoom() {
        finish();
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
        if (isStartLive) {
            logoutChatRoom();
        } else {
            releaseRtc(true, false);
            clearChatRoom();
        }
    }



    @Override
    protected void onDestroy() {
        AVChatManager.getInstance().observeAVChatState(this, false);
        super.onDestroy();

    }
}
