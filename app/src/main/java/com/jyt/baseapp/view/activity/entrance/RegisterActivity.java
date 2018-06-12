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
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.model.impl.RegisterModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.activity.BaseMCVActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseMCVActivity {


    @BindView(R.id.et_register_tel)
    EditText mEtTel;
    @BindView(R.id.et_register_vcode)
    EditText mEtVcode;
    @BindView(R.id.tv_register_getcode)
    TextView mTvGetcode;
    @BindView(R.id.et_register_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_register_repwd)
    EditText mEtRepwd;
    @BindView(R.id.et_icode)
    EditText mEtIcode;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    private RegisterModel mRegisterModel;
    private int State_getCode;//判断是否能获取验证码，0为可以，1为不可以
    private VcodeThread mVcodeThread;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
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
        setTextTitle("注册新用户");
        setFunctionText("提交");
        showFunctionImage();
        setvMainBackground(R.mipmap.bg_entrance);
        mRegisterModel = new RegisterModelImpl();
        mRegisterModel.onStart(this);
    }

    private void initSetting(){
        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {

                String inputTel = mEtTel.getText().toString();//手机号码
                String inputVCode = mEtVcode.getText().toString();//验证码
                String inputPwd = mEtPwd.getText().toString();//密码
                String inputRwd = mEtRepwd.getText().toString();//确认密码
                String inputICode = mEtIcode.getText().toString();//邀请码(非必要)
                if (TextUtils.isEmpty(inputTel)){
                    BaseUtil.makeText("手机号码不能为空");
                    mEtTel.setFocusable(true);
                    mEtTel.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(inputVCode)){
                    BaseUtil.makeText("验证码不能为空");
                    mEtVcode.setFocusable(true);
                    mEtVcode.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(inputPwd)){
                    BaseUtil.makeText("密码不能为空");
                    mEtPwd.setFocusable(true);
                    mEtPwd.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(inputRwd)){
                    BaseUtil.makeText("确认不能为空");
                    mEtRepwd.setFocusable(true);
                    mEtRepwd.requestFocus();
                    return;
                }

                if (!inputPwd.equals(inputRwd)){
                    BaseUtil.makeText("两次密码不一致");
                    return;
                }
                if (TextUtils.isEmpty(inputVCode)){
                    BaseUtil.makeText("验证码不能为空");
                }
                //验证结束
                IntentHelper.OpenPerfectInfoActivity(RegisterActivity.this,inputTel,inputICode,inputPwd,inputVCode);

            }
        });
    }



    class VcodeThread extends Thread {
        private int second = 60;

        @Override
        public synchronized void start() {
            super.start();
            State_getCode = 1;
            mTvGetcode.setTextColor(getResources().getColor(R.color.white));
            mTvGetcode.setText(second+"秒后重新获取");
        }

        @Override
        public void run() {
            super.run();
            while (second>=0){
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

    @OnClick(R.id.tv_register_getcode)
    public void getVCode(){
       if (State_getCode==0){
            String inputTel = mEtTel.getText().toString();
           if (!TextUtils.isEmpty(inputTel)){
               mVcodeThread = new VcodeThread();
               mVcodeThread.start();
               //获取验证码
               mRegisterModel.GetInviteCode(inputTel, new BeanCallback<BaseJson>() {
                   @Override
                   public void response(boolean success, BaseJson response, int id) {

                   }
               });
           }else {
               BaseUtil.makeText("请输入手机号码");
           }
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegisterModel.onDestroy();
    }
}
