package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LoginModel;
import com.jyt.baseapp.model.impl.LoginModelImpl;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.widget.SwitchView;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseMCVActivity {

    @BindView(R.id.sv_setting_n1)
    SwitchView mSvN1;
    @BindView(R.id.sv_setting_n2)
    SwitchView mSvN2;
    @BindView(R.id.sv_setting_n3)
    SwitchView mSvN3;
    @BindView(R.id.sv_setting_t1)
    SwitchView mSvT1;
    @BindView(R.id.sv_setting_t2)
    SwitchView mSvT2;
    @BindView(R.id.sv_setting_t3)
    SwitchView mSvT3;
    @BindView(R.id.sv_setting_m1)
    SwitchView mSvM1;
    @BindView(R.id.sv_setting_m2)
    SwitchView mSvM2;
    @BindView(R.id.sv_setting_m3)
    SwitchView mSvM3;
    @BindView(R.id.sv_setting_v1)
    SwitchView mSvV1;
    @BindView(R.id.sv_setting_v2)
    SwitchView mSvV2;
    @BindView(R.id.sv_setting_v3)
    SwitchView mSvV3;
    @BindView(R.id.rl_setting_help)
    RelativeLayout mRlHelp;
    @BindView(R.id.rl_setting_feedback)
    RelativeLayout mRlFeedback;
    @BindView(R.id.rl_setting_clean)
    RelativeLayout mRlClean;
    @BindView(R.id.rl_setting_black)
    RelativeLayout mRlSettingBlack;
    @BindView(R.id.rl_setting_logoff)
    RelativeLayout mRlSettingLogOff;

    private LoginModel mLoginModel;
    private Vibrator mVibrator;
    private long[] mPatter;
    private IPhoneDialog mLogOffDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
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
        setTextTitle("设置");
        setvMainBackgroundColor(R.color.bg_content);
        mLoginModel = new LoginModelImpl();
        mLoginModel.onStart(this);
        mVibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        mPatter = new long[]{1000, 1000, 2000, 50};
        mLogOffDialog = new IPhoneDialog(this);

    }

    private void initSetting() {
        mSvN1.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                if (isOpened) {
                    mVibrator.vibrate(mPatter, 0);
                } else {
                    mVibrator.cancel();
                }
            }
        });
        mLogOffDialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                mLoginModel.LogOff(new BeanCallback<BaseJson>(SettingActivity.this,false,null) {
                    @Override
                    public void response(boolean success, BaseJson response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                Const.LogOff(SettingActivity.this);
                            }
                        }
                    }
                });
            }

            @Override
            public void ClickCancel() {

            }
        });
    }

    @OnClick(R.id.rl_setting_feedback)
    public void OpenFeedBackActivity(){
        IntentHelper.OpenFeedBackActivity(this);
    }

    @OnClick(R.id.rl_setting_black)
    public void OpenBlackActivity(){
        IntentHelper.OpenBlackActivity(this);
    }

    @OnClick(R.id.rl_setting_logoff)
    public void LogOff(){
        mLogOffDialog.show();
    }


}
