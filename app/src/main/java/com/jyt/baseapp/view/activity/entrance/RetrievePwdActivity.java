package com.jyt.baseapp.view.activity.entrance;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.model.impl.RegisterModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.activity.BaseMCVActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RetrievePwdActivity extends BaseMCVActivity {
    @BindView(R.id.et_retrieve_tel)
    EditText mEtTel;
    @BindView(R.id.et_retrieve_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_retrieve_repwd)
    EditText mEtRePwd;
    @BindView(R.id.et_retrieve_vcode)
    EditText mEtVCode;
    @BindView(R.id.tv_retrieve_getcode)
    TextView mTvGetcode;

    private RegisterModel mRegisterModel;
    private PersonModel mPersonModel;
    private int State_getCode;//判断是否能获取验证码，0为可以，1为不可以
    private VcodeThread mVcodeThread;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrieve_pwd;
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
        setTextTitle("找回密码");
        setFunctionText("提交");
        showFunctionImage();
        setvMainBackground(R.mipmap.bg_entrance);
        mRegisterModel = new RegisterModelImpl();
        mRegisterModel.onStart(this);
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
    }

    private void initSetting(){
        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {
                String tel = mEtTel.getText().toString();
                String code = mEtVCode.getText().toString();
                String pwd = mEtPwd.getText().toString();
                String repwd = mEtRePwd.getText().toString();
                if (TextUtils.isEmpty(tel)){
                    BaseUtil.makeText("手机号码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(code)){
                    BaseUtil.makeText("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pwd)){
                    BaseUtil.makeText("新密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(repwd)){
                    BaseUtil.makeText("请再次输入新密码");
                    return;
                }
                if (!pwd.equals(repwd)){
                    BaseUtil.makeText("两次密码输入不一致");
                    return;
                }
                mPersonModel.forgetPwd(tel, code, pwd, new BeanCallback() {
                    @Override
                    public void response(boolean success, Object response, int id) {

                    }
                });
            }
        });
    }

    @OnClick(R.id.tv_retrieve_getcode)
    public void clickGetCode(){
        if (State_getCode==0){
            String inputTel = mEtTel.getText().toString();
            if (!TextUtils.isEmpty(inputTel)){
                mVcodeThread = new VcodeThread();
                mVcodeThread.start();
                //获取验证码
                mRegisterModel.GetInviteCode(inputTel, new BeanCallback<BaseJson<String>>() {
                    @Override
                    public void response(boolean success, BaseJson<String> response, int id) {
                        if (success && response.getCode()==200){
                            BaseUtil.makeText("修改成功");
                            finish();
                        }
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
        mRegisterModel.onDestroy();
        mPersonModel.onDestroy();
        super.onDestroy();
    }
}
