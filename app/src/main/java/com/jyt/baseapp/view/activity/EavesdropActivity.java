package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.BarrageSanAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.CallBean;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.TimeUtil;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.widget.BarrageMessage;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


public class EavesdropActivity extends BaseMCVActivity implements AVChatStateObserverLite {

    @BindView(R.id.tv_eavesdrop_time)
    TextView mTvTime;
    @BindView(R.id.tv_eavesdrop_money)
    TextView mTvMoney;
    @BindView(R.id.tv_eavesdrop_num)
    TextView mTvNum;
    @BindView(R.id.btn_eavesdrop_close)
    Button mBtnClose;
    @BindView(R.id.btn_eavesdrop_switch)
    Button mBtnSwitch;
    @BindView(R.id.rv_eavesdrop_s1)
    RecyclerView mRvS1;
    @BindView(R.id.rv_eavesdrop_s2)
    RecyclerView mRvS2;
    @BindView(R.id.rv_eavesdrop_s3)
    RecyclerView mRvS3;
    @BindView(R.id.tv_eavesdrop_t1)
    TextView mTvT1;
    @BindView(R.id.tv_eavesdrop_t2)
    TextView mTvT2;
    @BindView(R.id.tv_eavesdrop_t3)
    TextView mTvT3;
    @BindView(R.id.ll_eavesdrop_bottom)
    LinearLayout mLlEavesdropBottom;




    private LiveModel mLiveModel;
    private UserBean mUserBean;
    private boolean isHangUp;//是否挂断电话
    private boolean isStartLive;//是否处于连线
    private boolean isReady;//是否处于准备状态
    private BarrageSanAdapter mSanAdapter1;
    private BarrageSanAdapter mSanAdapter2;
    private BarrageSanAdapter mSanAdapter3;
    private boolean isShow1;
    private boolean isShow2;
    private boolean isShow3;
    //计时器
    private Handler mhandle = new Handler();
    private boolean isPause = false;//是否暂停
    private long currentSecond = 0;//当前毫秒数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eavesdrop;
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
        HideActionBar();
        setvMainBackground(R.mipmap.bg_entrance);
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);
        AVChatManager.getInstance().observeAVChatState(this, true);
        mRvS1.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        mRvS2.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        mRvS3.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        mSanAdapter1 = new BarrageSanAdapter();
        mSanAdapter2 = new BarrageSanAdapter();
        mSanAdapter3 = new BarrageSanAdapter();
        mRvS1.setAdapter(mSanAdapter1);
        mRvS2.setAdapter(mSanAdapter2);
        mRvS3.setAdapter(mSanAdapter3);
    }

    private void initSetting() {
        JoinEavesdropRoom();
        mSanAdapter1.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                if (mUserBean==null){
                    BaseUtil.makeText("无法使用");
                    return;
                }
                BarrageMessage bm = new BarrageMessage(Const.getUserNick(),(String) holder.getData(),null);
                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM
                        , mUserBean.getRoomName(), bm, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                Log.e("@#","发送成功");
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                            }
                        });
            }
        });

        mSanAdapter2.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                if (mUserBean==null){
                    BaseUtil.makeText("无法使用");
                    return;
                }
                BarrageMessage bm = new BarrageMessage(Const.getUserNick(),(String) holder.getData(),null);
                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM
                        , mUserBean.getRoomName(), bm, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                Log.e("@#","发送成功");
                                BaseUtil.makeText("发送成功");
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                            }
                        });
            }
        });

        mSanAdapter3.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                if (mUserBean==null){
                    BaseUtil.makeText("无法使用");
                    return;
                }
                BarrageMessage bm = new BarrageMessage(Const.getUserNick(),(String) holder.getData(),null);
                RongIMClient.getInstance().sendMessage(Conversation.ConversationType.CHATROOM
                        , mUserBean.getRoomName(), bm, null, null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {
                                Log.e("@#","发送成功");
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                            }
                        });
            }
        });

        mLiveModel.GetBarrageList(new BeanCallback<BaseJson>() {
            @Override
            public void response(boolean success, BaseJson response, int id) {
                if (success && response.getCode() == 200) {
                    List<List<String>> data = (List<List<String>>) response.getData();
                    mSanAdapter1.setDataList(data.get(0));
                    mSanAdapter2.setDataList(data.get(1));
                    mSanAdapter3.setDataList(data.get(2));
                    mSanAdapter1.notifyDataSetChanged();
                    mSanAdapter2.notifyDataSetChanged();
                    mSanAdapter3.notifyDataSetChanged();
                }
            }
        });

        mTvT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShow1){
                    mRvS1.setVisibility(View.VISIBLE);
                    isShow1 = true;
                }else {
                    mRvS1.setVisibility(View.INVISIBLE);
                    isShow1 = false;
                }
            }
        });

        mTvT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShow2){
                    mRvS2.setVisibility(View.VISIBLE);
                    isShow2 = true;
                }else {
                    mRvS2.setVisibility(View.INVISIBLE);
                    isShow2 = false;
                }
            }
        });

        mTvT3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShow3){
                    mRvS3.setVisibility(View.VISIBLE);
                    isShow3 = true;
                }else {
                    mRvS3.setVisibility(View.INVISIBLE);
                    isShow3 = false;
                }
            }
        });
    }

    private void JoinEavesdropRoom() {
        if (isReady) {
            BaseUtil.makeText("正在初始化，请稍后");
            return;
        }
        //偷听步骤
        //1、搜寻当前正在直播的用户，获取User数据
        //2、发起makeCall，获取通话记录ID（trId），加入房间
        currentSecond = 0;//初始化通话时间
        mLiveModel.EavesdropLive(new BeanCallback<BaseJson<UserBean>>() {
            @Override
            public void response(boolean success, BaseJson<UserBean> response, int id) {
                if (success && response.getCode() == 200) {
                    mUserBean = response.getData();
                    if (mUserBean == null) {
                        BaseUtil.makeText("当前无直播");

                    }
                    if (mUserBean == null) {
                        return;
                    }
                    mLiveModel.MakeCall(mUserBean.getId(), 1, new BeanCallback<BaseJson<CallBean>>() {
                        @Override
                        public void response(boolean success, BaseJson<CallBean> response, int id) {

                            if (success && response.getCode() == 200) {
                                ScannerManager.trId = String.valueOf(response.getData().getId());
                                AVChatManager.getInstance().enableRtc();
                                AVChatManager.getInstance().joinRoom2(mUserBean.getRoomName(), AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
                                    @Override
                                    public void onSuccess(AVChatData avChatData) {
                                        Log.e("@#", "join channel success");
                                        AVChatManager.getInstance().enableAudienceRole(true);
                                        isStartLive = true;
                                        mhandle.post(timeRunable);
                                        //加入弹幕聊天室
                                        RongIMClient.getInstance().joinChatRoom(mUserBean.getRoomName(), -1, new RongIMClient.OperationCallback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailed(int i) {
                                        Log.e("@#", "join channel failed, code:" + i);
                                    }

                                    @Override
                                    public void onException(Throwable throwable) {
                                        Log.e("@#", "join channel exception, throwable:" + throwable.getMessage());
                                    }
                                });
                            }
                        }
                    });

                } else {
                    BaseUtil.makeText("搜寻失败，请重试");
                }
            }
        });

    }

    /**
     * 离开房间
     */
    private void LeaveEavesdropRoom() {
        isPause = true;
        isReady = true;
        AVChatManager.getInstance().disableRtc();
        //挂断电话
        mLiveModel.DoneHangUp(new BeanCallback() {
            @Override
            public void response(boolean success, Object response, int id) {

            }
        });
        AVChatManager.getInstance().leaveRoom2(mUserBean.getRoomName(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isReady = false;
                if (isHangUp) {
                    //挂断操作，进入结算界面
                    IntentHelper.OpenEndCallActivity(EavesdropActivity.this, TimeUtil.getFormatMS(currentSecond));
                    finish();
                } else {
                    //切换偷听对象，离开房间后再次进入另一个房间
                    JoinEavesdropRoom();
                }
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }


    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            currentSecond = currentSecond + 1000;
            mTvTime.setText(TimeUtil.getFormatHMS(currentSecond));
            if (!isPause) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandle.postDelayed(this, 1000);
            }
        }
    };

    @OnClick(R.id.btn_eavesdrop_close)
    public void CloseEavesDrop() {
        //挂断电话
        isHangUp = true;
        if (isStartLive){
            LeaveEavesdropRoom();
        }else {
            finish();
        }

    }

    @OnClick(R.id.btn_eavesdrop_switch)
    public void SwithComUser() {
        isHangUp = false;
        LeaveEavesdropRoom();
    }


    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {

    }

    @Override
    public void onUserJoined(String s) {
        mLiveModel.getEavesdropNum(mUserBean.getId(), new BeanCallback() {
            @Override
            public void response(boolean success, Object response, int id) {

            }
        });
    }

    @Override
    public void onUserLeave(String s, int i) {
        if (s.equals(mUserBean.getEasyId())) {
            //主播离开时，用户同步离开当前界面
            isHangUp = true;
            LeaveEavesdropRoom();
        }
    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onProtocolIncompatible(int i) {

    }

    @Override
    public void onDisconnectServer(int i) {
        LeaveEavesdropRoom();
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
        return false;
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
        mLiveModel.onDestroy();
        AVChatManager.getInstance().observeAVChatState(this, false);
    }


}
