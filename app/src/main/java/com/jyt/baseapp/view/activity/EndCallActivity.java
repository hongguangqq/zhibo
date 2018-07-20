package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.SettlementBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class EndCallActivity extends BaseMCVActivity {
    @BindView(R.id.tv_endcall_time)
    TextView mTvTime;
    @BindView(R.id.tv_endcall_gmoney)
    TextView mTvGMoney;
    @BindView(R.id.tv_endcall_cmoney)
    TextView mTvCMoney;
    @BindView(R.id.btn_endcall_back)
    Button mBtnBack;

    private boolean mIsLive;
    private String mFinishTime;
    private LiveModel mLiveModel;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_end_call;
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
        setvMainBackground(R.mipmap.bg_entrance);
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);
        Tuple tuple = IntentHelper.EndCallActivityGetPara(getIntent());
        mIsLive = (boolean) tuple.getItem1();
        mFinishTime = (String) tuple.getItem2();

    }

    private void initSetting(){
        if (TextUtils.isEmpty(mFinishTime)){
            mTvTime.setText(TimeUtil.getFormatMS(ScannerManager.endComTome-ScannerManager.starComTime));
        }else {
            mTvTime.setText(mFinishTime);
        }
        mLiveModel.DoneHangUp(new BeanCallback() {
            @Override
            public void response(boolean success, Object response, int id) {
                mLiveModel.getComFinishMoney(mIsLive, new BeanCallback<BaseJson<SettlementBean>>() {
                    @Override
                    public void response(boolean success, BaseJson<SettlementBean> response, int id) {
                        if (success && response.getCode()==200){
                            SettlementBean bean  = response.getData();
                            if (mIsLive){
                                mTvGMoney.setText("获得金币："+bean.getGiftMoney());
                                mTvCMoney.setText("获得金币："+bean.getTalkMoney());
                            }else {
                                mTvGMoney.setText("消费金币："+bean.getGiftMoney());
                                mTvCMoney.setText("消费金币："+bean.getTalkMoney());
                            }
                        }
                    }
                });
            }
        });


    }

    @OnClick(R.id.btn_endcall_back)
    public void ClickBack(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveModel.onDestroy();
    }
}
