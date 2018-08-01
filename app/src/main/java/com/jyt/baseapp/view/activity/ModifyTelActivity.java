package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.model.impl.RegisterModelImpl;
import com.jyt.baseapp.util.BaseUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class ModifyTelActivity extends BaseMCVActivity {

    @BindView(R.id.et_modifyBind_tel)
    EditText mEtTel;
    @BindView(R.id.et_modifyBind_vcode)
    EditText mEtVcode;
    @BindView(R.id.tv_modifyBind_getcode)
    TextView mTvGetcode;

    private PersonModel mPersonModel;
    private RegisterModel mRegisterModel;
    private int State_getCode;//判断是否能获取验证码，0为可以，1为不可以
    private VcodeThread mVcodeThread;
    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_tel;
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
        setTextTitle("改绑手机");
        setFunctionText("提交");
        setvMainBackground(R.mipmap.bg_entrance);
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mRegisterModel = new RegisterModelImpl();
        mRegisterModel.onStart(this);
    }

    private void initSetting() {
        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {
                String phone = mEtTel.getText().toString();
                String code = mEtVcode.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    BaseUtil.makeText("请输入新的手机号码");
                    return;
                }
                if (BaseUtil.checkCellphone(phone)){
                    BaseUtil.makeText("手机号码格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(code)){
                    BaseUtil.makeText("验证码不能为空");
                    return;
                }
                mPersonModel.mofidyTel(phone, code, new BeanCallback<BaseJson>() {
                    @Override
                    public void response(boolean success, BaseJson response, int id) {
                        if (success && response.getCode()==200){
                            BaseUtil.makeText("改绑成功，请重新登录");
                            Const.LogOff(getContext());
                            IntentHelper.OpenLoginActivity(ModifyTelActivity.this);
                        }
                    }
                });

            }
        });
    }

    @OnClick(R.id.tv_modifyBind_getcode)
    public void getVCode(){
        if (State_getCode==0){
            String inputTel = mEtTel.getText().toString();
            if (!TextUtils.isEmpty(inputTel)){
                mVcodeThread = new VcodeThread();
                mVcodeThread.start();
                //获取验证码
                mRegisterModel.GetInviteCode(inputTel, new BeanCallback<BaseJson<String>>() {
                    @Override
                    public void response(boolean success, BaseJson<String> response, int id) {

                    }
                });
            }else {
                BaseUtil.makeText("请输入手机号码");
            }
        }
    }

    class VcodeThread extends Thread {
        private int second = 60;

        @Override
        public synchronized void start() {
            super.start();
            State_getCode=1;
            mTvGetcode.setTextColor(getResources().getColor(R.color.white));
            mTvGetcode.setText(second+"秒后重新获取");
        }

        @Override
        public void run() {
            super.run();
            while (second>0){
                try {
                    Thread.sleep(1000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTvGetcode.setText(second+"秒后重新获取");
                        }
                    });
                    second--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    State_getCode = 0;
                }
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    State_getCode = 0;
                    mTvGetcode.setTextColor(getResources().getColor(R.color.text_press));
                    mTvGetcode.setText("获取验证码");
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        mPersonModel.onDestroy();
        mRegisterModel.onDestroy();
        super.onDestroy();
    }
}
