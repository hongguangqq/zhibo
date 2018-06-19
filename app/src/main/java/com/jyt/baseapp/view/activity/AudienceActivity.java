package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jyt.baseapp.R;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;

import org.yczbj.ycvideoplayerlib.VideoPlayer;


public class AudienceActivity extends AppCompatActivity {

    private AVChatSurfaceViewRenderer videoRender;

    // 播放器
    private VideoPlayer videoPlayer;
    // state
    private boolean isStartLive = false; // 推流是否开始
    private boolean isMyVideoLink = true; // 观众连麦模式，默认视频
    private boolean isMyAlreadyApply = false; // 我是否已经申请连麦
    private boolean isAgreeToLink = false; // 主播是否同意我连麦（为了确保权限时使用）


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience);
        videoRender = findViewById(R.id.video_render);
        videoRender.setZOrderMediaOverlay(false);
    }
}
