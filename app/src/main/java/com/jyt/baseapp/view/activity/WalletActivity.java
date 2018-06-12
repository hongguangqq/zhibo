package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.WalletModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.model.impl.WalletModelImpl;

import butterknife.BindView;
import butterknife.OnClick;


public class WalletActivity extends BaseMCVActivity {

    @BindView(R.id.tv_wallet_number)
    TextView mTvNumber;
    @BindView(R.id.btn_wallet_account)
    Button mBtnAccount;
    @BindView(R.id.btn_wallet_putforward)
    Button mBtnPutforward;
    @BindView(R.id.btn_wallet_recharge)
    Button mBtnRecharge;

    private PersonModel mPersonModel;
    private WalletModel mWalletModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet;
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
        setTextTitle("我的钱包");
        setvMainBackgroundColor(R.color.bg_content);
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mWalletModel = new WalletModelImpl();
        mWalletModel.onStart(this);
    }

    private void initSetting() {
        mWalletModel.getBalance(new BeanCallback<BaseJson<Double>>() {
            @Override
            public void response(boolean success, BaseJson<Double> response, int id) {
                mTvNumber.setText(String.valueOf(response.getData()));
            }
        });

    }


    @OnClick(R.id.btn_wallet_recharge)
    public void OpenRechargeActivity(){
        IntentHelper.OpenRechargeActivity(this);
    }

    @OnClick(R.id.btn_wallet_putforward)
    public void OpenPutForwardActivity(){
        IntentHelper.OpenPutForwardActivity(this);
    }

    @OnClick(R.id.btn_wallet_account)
    public void OpenAccountActivity(){
        IntentHelper.OpenAccountActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonModel.onDestroy();
        mWalletModel.onDestroy();
    }
}
