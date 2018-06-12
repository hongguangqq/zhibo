package com.jyt.baseapp.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.ComAdapter;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.SpacesItemDecoration;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.RecordDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static com.jyt.baseapp.api.Const.PERMISSIONS_REQUEST_FOR_AUDIO;


public class CommunicationActivity extends BaseMCVActivity {

    @BindView(R.id.rv_com_content)
    RecyclerView mRvContent;
    @BindView(R.id.iv_com_voice)
    ImageView mIvVoice;
    @BindView(R.id.et_com_input)
    EditText mEtInput;
    @BindView(R.id.iv_com_add)
    ImageView mIvAdd;
    @BindView(R.id.tv_com_submit)
    TextView mTvSubmit;

    /*录音*/
    private RecordDialog mRecordDialog;
    //是否上滑取消
    private boolean isCancel;
    //线程操作
    private ExecutorService mExecutorService;
    //录音API
    private MediaRecorder mMediaRecorder;
    //录音开始时间与结束时间
    private long startTime, endTime;
    //录音所保存的文件
    private File mAudioFile;
    //当前是否正在播放
    private boolean isPlayAudio;
    //播放音频文件API
    private MediaPlayer mediaPlayer;
    //使用Handler更新UI线程
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //录音成功
                case Const.RECORD_SUCCESS:
                    Log.e("@#","录音成功");
                    mRecordDialog.dismiss();
                    break;
                //录音失败
                case Const.RECORD_FAIL:
                    Log.e("@#","录音失败");
                    mRecordDialog.dismiss();
                    break;
                //录音时间太短
                case Const.RECORD_TOO_SHORT:
                    BaseUtil.makeText("录音时间太短，请重新录音");
                    mRecordDialog.dismiss();
                    break;
                case Const.PLAY_COMPLETION:

                    break;
                case Const.PLAY_ERROR:

                    break;
                case Const.RECORD_CANCEL:
                    Log.e("@#","录音取消");
                    mRecordDialog.dismiss();
                    break;

            }
        }
    };


    private String comid;
    private ComAdapter mComAdapter;
    private List<Message> mMessageList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_communication;
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
        setFunctionImage(R.mipmap.icon_comvideo);
        showFunctionImage();
        setvMainBackgroundColor(R.color.bg_content);
        Tuple tuple = IntentHelper.CommunicationActivityGetPara(getIntent());
        comid = String.valueOf(tuple.getItem1());
        mMessageList = new ArrayList<>();
        mComAdapter = new ComAdapter();
        mRecordDialog = new RecordDialog(this);
        mExecutorService = Executors.newSingleThreadExecutor();


    }

    private void initSetting(){
        mComAdapter.setDataList(mMessageList);
        mRvContent.setAdapter(mComAdapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvContent.addItemDecoration(new SpacesItemDecoration(0, BaseUtil.dip2px(25)));
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    mIvAdd.setVisibility(View.GONE);
                    mTvSubmit.setVisibility(View.VISIBLE);
                }else {
                    mTvSubmit.setVisibility(View.GONE);
                    mIvAdd.setVisibility(View.VISIBLE);
                }
            }
        });

        RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, comid, -1, 100, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                mMessageList = messages;
                mComAdapter.notifyData(mMessageList);
//                mRvContent.scrollToPosition(mMessageList.size()-1);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

    }


    @OnClick(R.id.tv_com_submit)
    public void SendContent(){
        String content = mEtInput.getText().toString();
        if (!TextUtils.isEmpty(content)){
            TextMessage textMessage = new TextMessage(content);
            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, comid, textMessage, null, null, new IRongCallback.ISendMessageCallback() {
                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onSuccess(Message message) {
                    Log.e("@#","success");
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }



    @OnTouch(R.id.iv_com_voice)
    public boolean SendVocie(View view, MotionEvent event){
        boolean ret = false;
        int action = event.getAction();
        float ey = event.getY();
        float ex = event.getX();
        float downY = 0 ;
        switch (view.getId()) {
            case R.id.iv_com_voice:
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        //开始录音
                        downY = ey;
                        Log.e("@#","开始录音");
                        if (Build.VERSION.SDK_INT > 22) {
                            permissionForM();
                        } else {
                            startRecord();
                        }
                        ret =true;
                        break;
                    case MotionEvent.ACTION_UP:
                        //结束录音
                        if (!isCancel){
                            stopRecord();
                        }else {
                            isCancel = false;
                            cancelRecord();
                        }
                        ret = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float currentY = event.getY();
                        if (downY - currentY >BaseUtil.dip2px(200)){
                            Log.e("@#","cancel");
                            if (mRecordDialog!=null){
                                mRecordDialog.setTitle("松开取消");
                            }
                            isCancel = true;
                        }else {
                            isCancel = false;
                            mRecordDialog.setTitle("录音开始"+"\n"+"上滑取消");
                        }
                        break;
                }
                break;
        }
        return ret;
    }

    /**
     * @description 开始进行录音
     * @author ldm
     * @time 2017/2/9 9:18
     */
    private void startRecord() {
        //异步任务执行录音操作
        mRecordDialog.show();
        mRecordDialog.setTitle("录音开始"+"\n"+"上滑取消");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //播放前释放资源
                releaseRecorder();
                //执行录音操作
                recordOperation();
            }
        });
    }

    /**
     * @description 结束录音操作
     * @author ldm
     * @time 2017/2/9 9:18
     */
    private void stopRecord() {
        if (mMediaRecorder!=null){
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
                mAudioFile.delete();
                releaseRecorder();
                mHandler.sendEmptyMessage(Const.RECORD_TOO_SHORT);
                return;
            }
            //记录停止时间
            endTime = System.currentTimeMillis();
            //录音时间处理，比如只有大于2秒的录音才算成功
            int time = (int) ((endTime - startTime) / 1000);
            if (time >= 3) {
                //录音成功,发Message
                android.os.Message message = new android.os.Message();
                message.arg1 = time;
                message.what = Const.RECORD_SUCCESS;
                mHandler.sendMessage(message);
            } else {
                mAudioFile = null;
                mHandler.sendEmptyMessage(Const.RECORD_TOO_SHORT);
            }
            //录音完成释放资源
            releaseRecorder();
        }
    }

    /**
     * 取消录制
     */
    private void cancelRecord(){
        if (mMediaRecorder!=null){
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
                mAudioFile.delete();
                releaseRecorder();
                mHandler.sendEmptyMessage(Const.RECORD_CANCEL);
                return;
            }
            if (mAudioFile!=null){
                mAudioFile.delete();
                mAudioFile = null;
            }
            //释放资源
            releaseRecorder();
        }
    }

    /**
     * @description 翻放录音相关资源
     * @author ldm
     * @time 2017/2/9 9:33
     */
    private void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * @description 录音操作
     * @author ldm
     * @time 2017/2/9 9:34
     */
    private void recordOperation() {
        //创建MediaRecorder对象
        mMediaRecorder = new MediaRecorder();
        //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        mAudioFile = new File(Const.mMainFile, System.currentTimeMillis() + ".m4a");
        //创建父文件夹
        mAudioFile.getParentFile().mkdirs();
        try {
            //创建文件
            mAudioFile.createNewFile();
            //配置mMediaRecorder相应参数
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
            mMediaRecorder.setAudioSamplingRate(44100);
            //设置声音数据编码格式,音频通用格式是AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置编码频率
            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置录音保存的文件
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //记录开始录音时间
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            recordFail();
        }
    }

    /**
     * @description 录音失败处理
     * @author ldm
     * @time 2017/2/9 9:35
     */
    private void recordFail() {
        mAudioFile = null;
        mHandler.sendEmptyMessage(Const.RECORD_FAIL);
    }

    /**
     * @description 开始播放音频文件
     * @author ldm
     * @time 2017/2/9 16:56
     */
    private void startPlay(String path) {
        isPlayAudio=true;
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(path);
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }

    }

    /**
     * @description 停止播放或播放失败处理
     * @author ldm
     * @time 2017/2/9 16:58
     */
    private void playEndOrFail(boolean isEnd) {
        isPlayAudio = false;
        if (isEnd) {
            mHandler.sendEmptyMessage(Const.PLAY_COMPLETION);
        } else {
            mHandler.sendEmptyMessage(Const.PLAY_ERROR);
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    /*******6.0以上版本手机权限处理***************************/
    /**
     * @description 兼容手机6.0权限管理
     * @author ldm
     * @time 2016/5/24 14:59
     */
    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_FOR_AUDIO);
        } else {
            startRecord();
        }
    }
}
