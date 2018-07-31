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
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.model.impl.RegisterModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.activity.BaseMCVActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class BindTelActivity extends BaseMCVActivity {


    @BindView(R.id.et_bind_tel)
    EditText mEtTel;
    @BindView(R.id.et_bind_vcode)
    EditText mEtVcode;
    @BindView(R.id.tv_bind_getcode)
    TextView mTvGetcode;

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
    private boolean mIsOperation;
    private String mQWId;
    private String mNick;
    private String mBirthday;
    private String mCity;
    private int mSex;
    private boolean mQOW;
    private String mMatchCode;
    private RegisterModel mRegisterModel;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_bindtel;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initSeting();
    }

    private void init() {
        setTextTitle("绑定手机");
        setFunctionText("提交");
        showFunctionImage();
        setvMainBackground(R.mipmap.bg_entrance);
        Tuple tuple =IntentHelper.BindTelActivityGetPara(getIntent());
        mQWId = (String) tuple.getItem1();
        mNick = (String) tuple.getItem2();
        mBirthday = (String) tuple.getItem3();
        mCity = (String) tuple.getItme4();
        mSex = (int) tuple.getItem5();
        mQOW = (boolean) tuple.getItem6();
        mRegisterModel = new RegisterModelImpl();
    }

    private void initSeting() {
        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {
                String inputTel = mEtTel.getText().toString();
                String inputVCode= mEtVcode.getText().toString();
                if (TextUtils.isEmpty(inputTel)){
                    BaseUtil.makeText("手机号码不能为空");

                }
                if (TextUtils.isEmpty(inputVCode)){
                    BaseUtil.makeText("验证码不能为空");
                    return;
                }
                if (mIsOperation){
                    return;
                }
                mIsOperation=true;
                mRegisterModel.RegisterByQW(mQWId, mQOW, inputTel, inputVCode, mNick, mBirthday, mCity, mSex, new BeanCallback<BaseJson<UserBean>>() {
                    @Override
                    public void response(boolean success, BaseJson<UserBean> response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                BaseUtil.makeText("注册成功,请完善您的资料");
                                BaseUtil.e("id="+response.getData().getId());
                                IntentHelper.OpenExplainActivity(BindTelActivity.this,response.getData().getId()+"");
                            }else if (response.getCode()==500){
                                BaseUtil.makeText(response.getMessage());
                            }
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.tv_bind_getcode)
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
}
