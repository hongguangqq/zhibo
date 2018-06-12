package com.jyt.baseapp.view.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;

import java.io.IOException;

import butterknife.BindView;

public class VideoActivity extends BaseMCVActivity  {

    @BindView(R.id.sf_video)
    SurfaceView mFaceView;
    private MediaPlayer mMediaPlayer;

    private String mPath;
    private SurfaceHolder mSurfaceHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
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
        setTextTitle("");
        setvMainBackground(R.mipmap.bg_entrance);
        Tuple tuple = IntentHelper.VideoActivityGetPara(getIntent());
        mPath = (String) tuple.getItem1();
    }

    private void initSetting(){

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mMediaPlayer.setDataSource(mPath);
            // 设置显示视频的SurfaceHolder

            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.setDisplay(mFaceView.getHolder());
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.seekTo(0);
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }
}
