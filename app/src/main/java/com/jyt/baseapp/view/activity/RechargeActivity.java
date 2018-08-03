package com.jyt.baseapp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.PayResult;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.model.WalletModel;
import com.jyt.baseapp.model.impl.WalletModelImpl;
import com.jyt.baseapp.util.BaseUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class RechargeActivity extends BaseMCVActivity {

    @BindView(R.id.rl_recharge_money1)
    RelativeLayout mRlMoney1;
    @BindView(R.id.rl_recharge_money2)
    RelativeLayout mRlMoney2;
    @BindView(R.id.rl_recharge_money3)
    RelativeLayout mRlMoney3;
    @BindView(R.id.rl_recharge_money4)
    RelativeLayout mRlMoney4;
    @BindView(R.id.ll_recharge_input)
    LinearLayout mLlInput;
    @BindView(R.id.et_recharge_input)
    EditText mEtInput;
    @BindView(R.id.iv_recharge1)
    ImageView mIvWallet1;
    @BindView(R.id.iv_recharge2)
    ImageView mIvWallet2;
    @BindView(R.id.btn_recharge)
    Button mBtnWallet;
    @BindView(R.id.rl_recharge_w)
    RelativeLayout mRlW;
    @BindView(R.id.rl_recharge_z)
    RelativeLayout mRlZ;

    private boolean isWeixin;
    private String mDiscountCode;
    private WalletModel mWalletModel;
    private InputMethodManager mInputKey;
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
        };
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
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
        setTextTitle("充值");
        setvMainBackgroundColor(R.color.bg_content);
        mWalletModel = new WalletModelImpl();
        mWalletModel.onStart(this);
        mInputKey = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    private void initSetting() {
        mRlMoney1.setTag(1);
        mRlMoney2.setTag(2);
        mRlMoney3.setTag(3);
        mRlMoney4.setTag(4);
        mEtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_p10));
                    ChangeDiscount(0);
                }else {
                    mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                }
            }
        });
    }


    @OnClick(R.id.rl_recharge_z)
    public void RechargeZ(){
        if (!isWeixin){
            return;
        }
        mIvWallet1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_xuanzhong));
        mIvWallet2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_xuanzhong_no));
        isWeixin=false;
    }

    @OnClick(R.id.rl_recharge_w)
    public void RechargeW(){
        if (isWeixin){
            return;
        }
        mIvWallet1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_xuanzhong_no));
        mIvWallet2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_xuanzhong));
        isWeixin=true;
    }

    @OnClick({R.id.rl_recharge_money1,R.id.rl_recharge_money2,R.id.rl_recharge_money3,R.id.rl_recharge_money4})
    public void Discount(View v){
        int code = (int) v.getTag();
        if (mInputKey != null) {
            View v1 = new View(this);
            ViewGroup g1 = (ViewGroup)getWindow().getDecorView();
            ViewGroup g2 = (ViewGroup)g1.getChildAt(0);
            g2.addView(v1);
            mInputKey.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        ChangeDiscount(code);
    }

    @OnClick(R.id.btn_recharge)
    public void recharge(){
        if (!isWeixin){
            //支付宝

            if (!TextUtils.isEmpty(mDiscountCode)){
                mWalletModel.aliPay(mDiscountCode, new BeanCallback<BaseJson<String>>() {
                    @Override
                    public void response(boolean success, BaseJson<String> response, int id) {
                        Log.e("@#","ali="+response.getData());
                        final String orderInfo = response.getData();
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(RechargeActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo,true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }
                });
            }else {
                BaseUtil.makeText("请输入充值金额");
            }

        }
    }


    public void ChangeDiscount(int code){
        mRlMoney1.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        mRlMoney2.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        mRlMoney3.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        mRlMoney4.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        switch (code){
            case 0:
                mDiscountCode=mEtInput.getText().toString();
                break;
            case 1:
                mRlMoney1.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode="1000";
                break;
            case 2:
                mRlMoney2.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode="2000";
                break;
            case 3:
                mRlMoney3.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode="10000";
                break;
            case 4:
                mRlMoney4.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode="20000";
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mWalletModel.onDestroy();
        super.onDestroy();

    }
}
