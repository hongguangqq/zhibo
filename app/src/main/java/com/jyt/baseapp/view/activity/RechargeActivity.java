package com.jyt.baseapp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jyt.baseapp.R;

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
    private int mDiscountCode;
    private InputMethodManager mInputKey;

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
        mInputKey = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mRlMoney1.setTag(1);
        mRlMoney2.setTag(2);
        mRlMoney3.setTag(3);
        mRlMoney4.setTag(4);
    }

    private void initSetting() {
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


    public void ChangeDiscount(int code){
        mRlMoney1.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        mRlMoney2.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        mRlMoney3.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        mRlMoney4.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_nol));
        switch (code){
            case 0:
                mDiscountCode=Integer.parseInt(mEtInput.getText().toString());
                break;
            case 1:
                mRlMoney1.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode=1000;
                break;
            case 2:
                mRlMoney2.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode=2000;
                break;
            case 3:
                mRlMoney3.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode=10000;
                break;
            case 4:
                mRlMoney4.setBackground(getResources().getDrawable(R.mipmap.pic_chongzhi_sel));
                mLlInput.setBackground(getResources().getDrawable(R.drawable.corners_g10));
                mEtInput.setText("");
                mDiscountCode=20000;
                break;
        }
    }


}
