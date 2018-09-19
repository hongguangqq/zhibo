package com.jyt.baseapp.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.PropagationBean;
import com.jyt.baseapp.helper.IntentHelper;

import java.util.List;

/**
 * @author LinWei on 2018/5/4 15:48
 */
public class PagePropagandaAdapter extends PagerAdapter {

    private List<PropagationBean> data;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private ProgressBar mLoadingPar;
    private final View mView;
    private ImageView mIvPlay;
    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String path = (String) view.getTag();
            IntentHelper.openBrowseImagesActivity(mContext,path);
        }
    };

    public PagePropagandaAdapter(Context context , List<PropagationBean> list){
        mContext = context;
        this.data = list;
        mView = View.inflate(mContext, R.layout.layout_play,null);
        mSurfaceView = (SurfaceView) mView.findViewById(R.id.sf_video);
        mIvPlay = (ImageView) mView.findViewById(R.id.iv_video);
        mLoadingPar = (ProgressBar) mView.findViewById(R.id.pb_video);
    }

    @Override
    public int getCount() {
//        return Integer.MAX_VALUE;
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        position=position%data.size();
        PropagationBean bean = data.get(position);
          String path=data.get(position).getPath();
            if (!bean.isVideo()){
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(mContext).load(path).into(imageView);
                imageView.setTag(path);
                imageView.setOnClickListener(mListener);
                container.addView(imageView);
                return imageView;
            }else{
                try {
                    mMediaPlayer = new MediaPlayer();
    //                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // 设置播放的视频源
                    mMediaPlayer.setDataSource(path);
                    // 设置显示视频的SurfaceHolder

                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
                            mMediaPlayer.start();
                            mLoadingPar.setVisibility(View.GONE);
                        }
                    });
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
    //                        mMediaPlayer.seekTo(0);
    //                        mMediaPlayer.start();
                            mMediaPlayer.stop();
                            mIvPlay.setVisibility(View.VISIBLE);
                        }
                    });
                    mIvPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mMediaPlayer.prepareAsync();
                            mLoadingPar.setVisibility(View.VISIBLE);
                            mIvPlay.setVisibility(View.GONE);
                        }
                    });
                    container.addView(mView);
                    return mView;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        return null;
    }


    public void VideoStop(){
        if (mMediaPlayer!=null){
            mMediaPlayer.seekTo(0);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mIvPlay.setVisibility(View.VISIBLE);
            mLoadingPar.setVisibility(View.GONE);
        }
    }


    public void setVideoData(String path){
        try {
            if (mMediaPlayer!=null){
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mMediaPlayer.setDataSource(path);
            // 设置显示视频的SurfaceHolder
//            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.setDisplay(mSurfaceView.getHolder());
                    mMediaPlayer.start();
                    mLoadingPar.setVisibility(View.GONE);
                }
            });
            mSurfaceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.seekTo(0);
                    mMediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPlayLogo(){
        mIvPlay.setVisibility(View.VISIBLE);
    }

    public MediaPlayer getMediaPlayer(){
        return mMediaPlayer;
    }

    public void MediaPlayerDestory(){
        if (mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void notifyData(List<PropagationBean> data){
        this.data = data;
        notifyDataSetChanged();
    }




}
