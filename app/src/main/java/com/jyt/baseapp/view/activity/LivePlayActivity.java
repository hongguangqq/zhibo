package com.jyt.baseapp.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCaptureOrientation;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoFrameRate;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQuality;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatLiveCompositingLayout;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;

import java.util.UUID;


public class LivePlayActivity extends BaseMCVActivity {
    private AVChatSurfaceViewRenderer mRenderer;
    private AVChatCameraCapturer mVideoCapturer;
    private String roomId;


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
        mRenderer = findViewById(R.id.video_render);
        startPreview();
        Log.e("@#",Const.getWyAccount()+"--"+Const.getWyToken());
        LoginInfo loginInfo = new LoginInfo(Const.getWyAccount(),Const.getWyToken());

        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object param) {
                Log.e("@#","Wy-onSuccess");
            }

            @Override
            public void onFailed(int code) {
                Log.e("@#","Wy-onFailed-code:"+code);
            }

            @Override
            public void onException(Throwable exception) {
                Log.e("@#","Wy-onException:"+exception.getMessage());
            }
        });
        final String meetingName = UUID.randomUUID().toString();
        Log.e("@#","room="+meetingName);
        AVChatManager.getInstance().createRoom(meetingName, null, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                Log.e("@#","room创建成功"+meetingName);


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


    //主播直播前预览
    private void startPreview() {
        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
            mVideoCapturer.setFlash(true);//设置闪光灯
        }
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
        if (true) {
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().setupLocalVideoRender(mRenderer, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            AVChatManager.getInstance().startVideoPreview();
        }
    }


}
