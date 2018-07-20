package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.BarrageAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.widget.BarrageMessage;
import com.jyt.baseapp.view.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

import static com.jyt.baseapp.App.getHandler;


public class LivePlayActivity extends BaseMCVActivity {
    private FrameLayout mFlLocalRender;
    private FrameLayout mFlRemoterRender;
    private TextView mTvName;
    private TextView mTvMoney;
    private CircleImageView mIvHpic;
    private ImageView mIvHorn;//音频开关
    private ImageView mIvCamera;//视频开关
    private ImageView mIvNarrow;//窗口模式开关
    private TextView mTvB1;
    private TextView mTvB2;
    private TextView mTvB3;
    private TextView mTvB4;
    private RecyclerView mRvDanmu;

    private LiveModel mLiveModel;
    private BarrageAdapter mBarrageAdapter;
    private List<BarrageMessage> mBarrageMessageList;

    private Button mBtnStar;
    private String mMeetingName;
    private IPhoneDialog mExitDialog;
    private IPhoneDialog mInputDialog;
    private Handler mVideoEffectHandler = new Handler();
    private boolean isMeLeave;//是否为自己主动发起离开





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
        HideActionBar();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        EventBus.getDefault().register(this);
        mFlLocalRender = findViewById(R.id.fl_localRender);
        mFlRemoterRender = findViewById(R.id.fl_remoteRender);
        mTvName = findViewById(R.id.tv_livePlay_name);
        mTvMoney = findViewById(R.id.tv_livePlay_money);
        mIvHpic = findViewById(R.id.iv_livePlay_hpic);
        mBtnStar = findViewById(R.id.btn_star);
        mIvHorn = findViewById(R.id.iv_live_horn);
        mIvCamera = findViewById(R.id.iv_live_camera);
        mIvNarrow = findViewById(R.id.iv_live_narrow);
        mTvB1 = findViewById(R.id.tv_livePlay_t1);
        mTvB2 = findViewById(R.id.tv_livePlay_t2);
        mTvB3 = findViewById(R.id.tv_livePlay_t3);
        mTvB4 = findViewById(R.id.tv_livePlay_t4);
        mRvDanmu = findViewById(R.id.rv_livePlay_danmu);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
        mInputDialog = new IPhoneDialog(this);
        mInputDialog.setInputShow(true);
        mInputDialog.setTitle("请输入发送的消息");
        mBarrageMessageList = new ArrayList<>();
        mBarrageAdapter = new BarrageAdapter();

        if (ScannerManager.isStartLive){
            mBtnStar.setVisibility(View.GONE);//隐藏直播开启按钮
        }
        if (mBtnStar.getVisibility()==View.GONE){
            JoinDanmuRoom();
        }
        mTvName.setText(Const.getUserNick());
        Glide.with(this).load(Const.getUserHeadImg()).error(R.mipmap.timg).into(mIvHpic);
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);
    }

    private void initStting(){
        ScannerManager.isBigScreen = true;//进入该界面默认为大屏显示
        if (!BaseUtil.isServiceRunning(this,"com.jyt.baseapp.service.ScannerService")){
            ScannerController.getInstance().startMonkServer(this);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ScannerController.getInstance().SwitchLive(true);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                if (ScannerManager.isStartLive){
                    mFlLocalRender.addView(ScannerController.getInstance().getLocalRender(),params);
                    mFlRemoterRender.addView(ScannerController.getInstance().getRemoteRender(),params);
                }else {
                    mFlRemoterRender.addView(ScannerController.getInstance().getLocalRender(),params);
                }

            }
        },1000);
        mBarrageAdapter.setDataList(mBarrageMessageList);
        mRvDanmu.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRvDanmu.setAdapter(mBarrageAdapter);


    }

    private void initListener(){
        mBtnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlRemoterRender.removeAllViews();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != ScannerController.getInstance().getLocalRender()){
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            //创建房间
                            if (!ScannerManager.isStartLive){
                                ScannerController.getInstance().createAndJoinRoom();
                            }
                            mFlLocalRender.addView(ScannerController.getInstance().getLocalRender(),params);
                            mFlRemoterRender.addView(ScannerController.getInstance().getRemoteRender(),params);
                            mBtnStar.setVisibility(View.GONE);//隐藏直播开启按钮
                            JoinDanmuRoom();
                        }

                    }
                }, 1000);
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

        mIvHorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerController.getInstance().muteLocalAudio();
            }
        });

        mIvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerController.getInstance().muteLocalVideo();
            }
        });

        mIvNarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ScannerManager.isAudienceJoin){
                    ScannerController.getInstance().show();
                    finish();
                }else {
                    BaseUtil.makeText("当前无用户进入，不可使用该功能");
                }

            }
        });

        mInputDialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                if (!TextUtils.isEmpty(input)){
                    final BarrageMessage bm = new BarrageMessage(Const.getUserNick(),input,null);
                    RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM, ScannerManager.mMeetingName, bm, "", null, new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {
                            Log.e("@#","成功");
                            mInputDialog.setInputText("");
                            mBarrageMessageList.add(bm);
                            mRvDanmu.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBarrageAdapter.notifyDataSetChanged();
                                }
                            });

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            Log.e("@#","失败："+errorCode.getMessage());
                        }
                    });
                }
            }

            @Override
            public void ClickCancel() {

            }
        });

        mTvB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputDialog.show();
            }
        });

        mTvB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询余额
                if (ScannerManager.LastRequestNum<3){
                    ScannerManager.LastRequestNum++;
                    mLiveModel.GetUserBlance(new BeanCallback<BaseJson<Double>>() {
                        @Override
                        public void response(boolean success, BaseJson<Double> response, int id) {
                            BaseUtil.makeText("观众还剩"+response.getData()+"硬币");
                        }
                    });
                }else {
                    BaseUtil.makeText("请求次数不可超过3次");
                }
            }
        });

        mTvB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.OpenNewActivity(LivePlayActivity.this, 5);
            }
        });

        mTvB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExitDialog.show();
            }
        });

        //监听聊天室消息的到来
        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(final io.rong.imlib.model.Message message, int i) {
                if (message.getTargetId().equals(ScannerManager.mMeetingName)){
                    if ("app:BarrageMsg".equals(message.getObjectName())) {
                        mRvDanmu.post(new Runnable() {
                            @Override
                            public void run() {
                                BarrageMessage barrageMessage = (BarrageMessage) message.getContent();
                                mBarrageMessageList.add(barrageMessage);
                                mBarrageAdapter.notifyDataSetChanged();
                                mRvDanmu.smoothScrollToPosition(mBarrageAdapter.getItemCount());
                            }
                        });

                    }

                }
                return false;
            }
        });
    }

    /**
     * 加入弹幕房
     */
    private void JoinDanmuRoom(){
        RongIMClient.getInstance().joinChatRoom(ScannerManager.mMeetingName, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.e("@#","进入弹幕房成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("@#","进入弹幕房失败:"+errorCode.getMessage());
            }
        });
    }



    // 退出聊天室
    private void logoutChatRoom() {
        if (mExitDialog.isShowing()){
            doCompletelyFinish();
        }else {
            mExitDialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventOver(EventBean bean) {
        if (Const.Event_Live.equals(bean.getCode())){
            if (isMeLeave){
                return;
            }
            finish();
            IntentHelper.OpenEndCallActivity(this,true);
        }else if (Const.Event_HangUp.equals(bean.getCode())){
            BaseUtil.makeText("观众已挂断");
            finish();
        }else if (Const.Event_UserJoin.equals(bean.getCode())){
            //用户加入
            mLiveModel.getEavesdropNum(Integer.valueOf(Const.getUserID()), new BeanCallback() {
                @Override
                public void response(boolean success, Object response, int id) {

                }
            });
        }else if (Const.Event_UserLeave.equals(bean.getCode())){
            //用户离开
        }
    }

    //计时器
    private  Handler mhandle;
    private  boolean isPause = false;//是否暂停
    private  Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            if (!isPause) {
                //联网报告

                //递归调用本runable对象，实现每隔60秒一次执行任务
                mhandle.postDelayed(this, 60*1000);

            }
        }
    };



    private void doCompletelyFinish() {
        ScannerManager.isStartLive = false;
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isMeLeave = true;
                ScannerController.getInstance().closeScanner(LivePlayActivity.this,true,true);
                IntentHelper.OpenEndCallActivity(LivePlayActivity.this,true);
                finish();
            }
        }, 50);
    }

    @Override
    public void onBackPressed() {
        if (ScannerManager.isBigScreen){
            //未处于悬浮窗状态
            if (ScannerManager.isStartLive){
                logoutChatRoom();
            }else {
                ScannerController.getInstance().closeScanner(LivePlayActivity.this,true,false);
                finish();
            }

        }else {
            //处于悬浮窗状态
        }


    }




    @Override
    protected void onDestroy() {
        if (ScannerManager.isBigScreen){
            //未处于悬浮窗状态
            if (ScannerManager.isStartLive){
                ScannerController.getInstance().closeScanner(LivePlayActivity.this,true,true);
            }
            ScannerController.getInstance().stopMonkServer(this);
        }else {
            //处于悬浮窗状态

        }
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        RongIMClient.getInstance().quitChatRoom(ScannerManager.mMeetingName, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }
}
