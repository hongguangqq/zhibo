package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LoginModel;
import com.jyt.baseapp.model.impl.LoginModelImpl;
import com.jyt.baseapp.util.CacheCleanUtil;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.dialog.LoadingDialog;
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
    @BindView(R.id.rl_setting_modifyBind)
    RelativeLayout mRlModifyBind;
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

        if (Const.getVisitorSound()){
            mSvN1.open();
        }else {
            mSvN1.close();
        }
        if (Const.getVisitorVibrate()){
            mSvN2.open();
        }else {
            mSvN2.close();
        }
        if (Const.getVisitorToast()){
            mSvN3.open();
        }else {
            mSvN3.close();
        }

        if (Const.getTxtSound()){
            mSvT1.open();
        }else {
            mSvT1.close();
        }
        if (Const.getTxtVibrate()){
            mSvT2.open();
        }else {
            mSvT2.close();
        }
        if (Const.getTxtToast()){
            mSvT3.open();
        }else {
            mSvT3.close();
        }

        if (Const.getSoundSound()){
            mSvM1.open();
        }else {
            mSvM1.close();
        }
        if (Const.getSoundVibrate()){
            mSvM2.open();
        }else {
            mSvM2.close();
        }
        if (Const.getSoundToast()){
            mSvM3.open();
        }else {
            mSvM3.close();
        }

        if (Const.getVideoSound()){
            mSvV1.open();
        }else {
            mSvV1.close();
        }
        if (Const.getVideoVibrate()){
            mSvV2.open();
        }else {
            mSvV2.close();
        }
        if (Const.getVideoToast()){
            mSvV3.open();
        }else {
            mSvV3.close();
        }
        

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
                Const.LogOff(SettingActivity.this);
//                mLoginModel.LogOff(new BeanCallback<BaseJson>(SettingActivity.this,false,null) {
//                    @Override
//                    public void response(boolean success, BaseJson response, int id) {
//                        if (success){
//                            if (response.getCode()==200){
//
//                            }
//                        }
//                    }
//                });
            }

            @Override
            public void ClickCancel() {

            }
        });
        mSvN1.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setVisitorSound(isOpened);
            }
        });
        mSvN2.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setVisitorVibrate(isOpened);
            }
        });
        mSvN3.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setVisitorToast(isOpened);
            }
        });
        mSvT1.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setTxtSound(isOpened);
            }
        });
        mSvT2.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setTxtVibrate(isOpened);
            }
        });
        mSvT3.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setTxtToast(isOpened);
            }
        });
        mSvM1.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setSoundSound(isOpened);
            }
        });
        mSvM2.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setSoundVibrate(isOpened);
            }
        });
        mSvM3.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setSoundToast(isOpened);
            }
        });
        mSvV1.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setVideoSound(isOpened);
            }
        });
        mSvV2.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setVideoVibrate(isOpened);
            }
        });
        mSvV3.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                Const.setVideoToast(isOpened);
            }
        });

    }

    @OnClick(R.id.rl_setting_clean)
    public void clickClear(){
        final IPhoneDialog dialog = new IPhoneDialog(this);
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        dialog.setTitle("确定清空缓存");
        dialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                dialog.dismiss();
                loadingDialog.show();
                CacheCleanUtil.clearAllCache(getApplicationContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        T.showShort(SettingActivity.this,"清除完毕");
                        loadingDialog.dismiss();
                    }
                }, 4000);
            }

            @Override
            public void ClickCancel() {

            }
        });
        dialog.show();
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

    @OnClick(R.id.rl_setting_modifyBind)
    public void clickModifyBind(){
        IntentHelper.OpenModifyTelActivity(this);
    }


}
