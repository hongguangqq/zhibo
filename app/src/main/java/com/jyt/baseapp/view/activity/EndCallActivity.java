package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.TimeUtil;

import butterknife.BindView;


public class EndCallActivity extends BaseMCVActivity {
    @BindView(R.id.tv_endcall_time)
    TextView mTvTime;
    @BindView(R.id.tv_endcall_gmoney)
    TextView mTvGMoney;
    @BindView(R.id.tv_endcall_cmoney)
    TextView mTvCMoney;

    private boolean mIsLive;
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

    }

    private void initSetting(){
        mTvTime.setText(TimeUtil.getFormatHMS(ScannerManager.endComTome-ScannerManager.starComTime));
        mLiveModel.getComFinishMoney(mIsLive, new BeanCallback() {
            @Override
            public void response(boolean success, Object response, int id) {
                if (mIsLive){

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveModel.onDestroy();
    }
}
