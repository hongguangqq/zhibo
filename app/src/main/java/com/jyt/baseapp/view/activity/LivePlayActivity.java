package com.jyt.baseapp.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.BaseRcvAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BarrageBean;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerController;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.jyt.baseapp.App.getHandler;


public class LivePlayActivity extends BaseMCVActivity {
    private FrameLayout mFlLocalRender;
    private FrameLayout mFlRemoterRender;
    private ImageView mIvHorn;//音频开关
    private ImageView mIvCamera;//视频开关
    private ImageView mIvNarrow;//窗口模式开关
    private TextView mTvB1;
    private TextView mTvB2;
    private TextView mTvB3;
    private TextView mTvB4;

    private LiveModel mLiveModel;

    private List<String> mBarrageStrList;//弹幕Str列表
    private List<BarrageBean> mBarrageBeanList;//弹幕列表
    private BaseRcvAdapter mBaseRcvAdapter;
    private OptionsPickerView mBarragePickerView;
    private Button mBtnStar;
    private String mMeetingName;
    private IPhoneDialog mExitDialog;
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
        mBtnStar = findViewById(R.id.btn_star);
        mIvHorn = findViewById(R.id.iv_live_horn);
        mIvCamera = findViewById(R.id.iv_live_camera);
        mIvNarrow = findViewById(R.id.iv_live_narrow);
        mTvB1 = findViewById(R.id.tv_livePlay_t1);
        mTvB2 = findViewById(R.id.tv_livePlay_t2);
        mTvB3 = findViewById(R.id.tv_livePlay_t3);
        mTvB4 = findViewById(R.id.tv_livePlay_t4);
        mExitDialog = new IPhoneDialog(this);
        mExitDialog.setTitle("确认退出吗？");
        mBarrageStrList = new ArrayList<>();
        if (ScannerManager.isStartLive){
            mBtnStar.setVisibility(View.GONE);//隐藏直播开启按钮
        }
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
                mFlRemoterRender.addView(ScannerController.getInstance().getLocalRender(),params);
                //                    ScannerController.getInstance().show();
            }
        },1000);
        mBarragePickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String txt = mBarrageStrList.get(options1);
                mLiveModel.SendBarrage(txt, new BeanCallback() {
                    @Override
                    public void response(boolean success, Object response, int id) {

                    }
                });
            }
        }).setSubmitText("发送")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("消息")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        mLiveModel.GetBarrageList(new BeanCallback<BaseJson<List<BarrageBean>>>() {
            @Override
            public void response(boolean success, BaseJson<List<BarrageBean>> response, int id) {
                    if (success && response.getCode()==200){
                        List<BarrageBean> List = response.getData();
                        for (BarrageBean bean:List){
                            mBarrageStrList.add(bean.getText());
                        }
                        mBarragePickerView.setPicker(mBarrageStrList);
                    }
            }
        });

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
//                            mFlRemoterRender.addView(ScannerController.getInstance().getRemoteRender(),params);
                            mBtnStar.setVisibility(View.GONE);//隐藏直播开启按钮
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

        mTvB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBarragePickerView.show();
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
            Log.e("@#","观众挂断");
            BaseUtil.makeText("观众已挂断");
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void BarrageUpdata(BarrageBean bean){
        //弹幕处理
        mBarrageBeanList.add(bean);
    }

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
    }
}
