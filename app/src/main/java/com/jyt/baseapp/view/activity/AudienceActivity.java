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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.BarrageAdapter;
import com.jyt.baseapp.adapter.BarrageGiftAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BarrageBean;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.WalletModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.model.impl.WalletModelImpl;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
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


public class AudienceActivity extends BaseMCVActivity {
    private static final String TAG = "@#";
    private FrameLayout mFlRemoterRender;
    private FrameLayout mFlLocalRender;
    private TextView mTvName;
    private TextView mTvMoney;
    private CircleImageView mIvHpic;
    private IPhoneDialog mExitDialog;
    private ImageView mIvHorn;//音频开关
    private ImageView mIvCamera;//视频开关
    private ImageView mIvNarrow;//窗口模式开关
    private TextView mTvB1;
    private TextView mTvB2;
    private TextView mTvB3;
    private TextView mTvB4;
    private RecyclerView mRvDanmu;
    private RecyclerView mRvGift;

    private LiveModel mLiveModel;
    private WalletModel mWalletModel;
    private boolean isMeLeave;//是否为自己主动发起离开
    private OptionsPickerView mBarragePickerView;
    private List<String> mBarrageStrList;//弹幕Str列表
    private IPhoneDialog mInputDialog;
    private BarrageAdapter mBarrageAdapter;
    private List<BarrageMessage> mBarrageMessageList;
    private List<BarrageBean> mBarrageGiftList;
    private BarrageGiftAdapter mBarrageGiftAdapter;
    private boolean mIsShow;



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
        initSetting();


    }

    private void init(){
        HideActionBar();
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        mFlLocalRender = findViewById(R.id.fl_LocalRender);
        mFlRemoterRender = findViewById(R.id.fl_RemoteRender);
        mIvHpic = findViewById(R.id.iv_audeience_hpic);
        mTvName = findViewById(R.id.tv_audience_name);
        mTvMoney = findViewById(R.id.tv_audience_money);
        mIvHorn = findViewById(R.id.iv_audeience_horn);
        mIvCamera = findViewById(R.id.iv_audeience_camera);
        mIvNarrow = findViewById(R.id.iv_audeience_narrow);
        mTvB1 = findViewById(R.id.tv_audeience_t1);
        mTvB2 = findViewById(R.id.tv_audeience_t2);
        mTvB3 = findViewById(R.id.tv_audeience_t3);
        mTvB4 = findViewById(R.id.tv_audeience_t4);
        mRvDanmu = findViewById(R.id.rv_audience_danmu);
        mRvGift = findViewById(R.id.rv_audience_gift);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
        mInputDialog = new IPhoneDialog(this);
        mInputDialog.setInputShow(true);
        mInputDialog.setTitle("请输入发送的消息");
        mBarrageMessageList = new ArrayList<>();
        mBarrageGiftList = new ArrayList<>();
        mBarrageAdapter = new BarrageAdapter();
        mBarrageGiftAdapter = new BarrageGiftAdapter();

        mTvName.setText(Const.getUserNick());
        Glide.with(this).load(Const.getUserHeadImg()).error(R.mipmap.timg).into(mIvHpic);
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);
        mWalletModel = new WalletModelImpl();
        mWalletModel.onStart(this);
    }




    private void initSetting(){
        ScannerManager.isBigScreen = true;
        if (!BaseUtil.isServiceRunning(this,"com.jyt.baseapp.service.ScannerService")){
            ScannerController.getInstance().startMonkServer(this);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!ScannerManager.isStartLive){
                    ScannerController.getInstance().joinRoom(ScannerManager.mMeetingName,ScannerManager.comID);
                    JoinDanmuRoom();
                }
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                mFlLocalRender.addView(ScannerController.getInstance().getLocalRender(),params);
                mFlRemoterRender.addView(ScannerController.getInstance().getRemoteRender(),params);
            }
        },1000);
        mBarrageAdapter.setDataList(mBarrageMessageList);
        mRvDanmu.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRvDanmu.setAdapter(mBarrageAdapter);
        mBarrageGiftAdapter.setDataList(mBarrageGiftList);
        mRvGift.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvGift.setAdapter(mBarrageGiftAdapter);

        mBarrageGiftAdapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                BarrageBean gift = (BarrageBean) holder.getData();
                final BarrageMessage bm  = new BarrageMessage(Const.getUserNick(),null,gift.getImg());
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
                ScannerController.getInstance().show();
                finish();

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
                if (!mIsShow){
                    mRvGift.setVisibility(View.VISIBLE);
                    mIsShow = true;
                } else {
                    mRvGift.setVisibility(View.GONE);
                    mIsShow = false;
                }
            }
        });

        mTvB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.OpenRechargeActivity(AudienceActivity.this);
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
            public boolean onReceived(io.rong.imlib.model.Message message, int i) {

                if (message.getTargetId().equals(ScannerManager.mMeetingName)){
                    if ("app:BarrageMsg".equals(message.getObjectName())) {
                        BarrageMessage barrageMessage = (BarrageMessage) message.getContent();
                        mBarrageMessageList.add(barrageMessage);
                        mBarrageAdapter.notifyDataSetChanged();
                    }

                }
                return false;
            }
        });

        mLiveModel.GetBarrageGift(new BeanCallback<BaseJson<List<BarrageBean>>>() {
            @Override
            public void response(boolean success, BaseJson<List<BarrageBean>> response, int id) {
                if (success && response.getCode() == 200){
                    Log.e("@#","获取礼物列表成功");
                    mBarrageGiftList = response.getData();
                    mBarrageGiftAdapter.setDataList(mBarrageGiftList);
                }
            }
        });
        mhandle.post(timeRunable);

    }

    //计时器
    private Handler mhandle = new Handler();
    private boolean isPause = false;//是否暂停
    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            if (!isPause) {
                //联网报告
                mWalletModel.getBalance(new BeanCallback<BaseJson<Double>>() {
                    @Override
                    public void response(boolean success, BaseJson<Double> response, int id) {
                        mTvMoney.setText("我的余额："+response.getData());
                    }
                });
                //递归调用本runable对象，实现每隔60秒一次执行任务
                mhandle.postDelayed(this, 60*1000);

            }
        }
    };

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventOver(EventBean bean) {
        if (Const.Event_Audience.equals(bean.getCode())){
            if (isMeLeave){
                return;
            }
            Log.e("@#","主播离开房间，直播结束");
            IntentHelper.OpenEndCallActivity(this,false);
            finish();
        }else if (Const.Event_UserJoin.equals(bean.getCode())){
            //新用户加入
            mLiveModel.getEavesdropNum(Integer.valueOf(Const.getUserID()), new BeanCallback() {
                @Override
                public void response(boolean success, Object response, int id) {

                }
            });
        }else if (Const.Event_UserLeave.equals(bean.getCode())){
            //用户离开
        }
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
        ScannerManager.isStartLive = false;
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isMeLeave = true;
                ScannerController.getInstance().closeScanner(AudienceActivity.this,true,true);
                IntentHelper.OpenEndCallActivity(AudienceActivity.this,false);
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
                ScannerController.getInstance().closeScanner(AudienceActivity.this,true,false);
                finish();
            }
        }else {
            //处于悬浮窗状态
            finish();
        }


    }



    @Override
    protected void onDestroy() {
        isPause = true;
        if (ScannerManager.isBigScreen){
            //未处于悬浮窗状态
            ScannerController.getInstance().stopMonkServer(this);
            if (ScannerManager.isStartLive){

            }
        }else {
            //处于悬浮窗状态

        }
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mLiveModel.onDestroy();
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
