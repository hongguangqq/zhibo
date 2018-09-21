package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.model.WalletModel;
import com.jyt.baseapp.model.impl.WalletModelImpl;
import com.jyt.baseapp.util.BaseUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class PutForwardActivity extends BaseMCVActivity {

    @BindView(R.id.et_putforward_money)
    EditText mEtMoney;
    @BindView(R.id.et_putforward_bank)
    EditText mEtBank;
    @BindView(R.id.et_putforward_number)
    EditText mEtNumber;
    @BindView(R.id.et_putforward_name)
    EditText mEtName;
    @BindView(R.id.et_putforward_tel)
    EditText mEtTel;
    @BindView(R.id.btn_putforward)
    Button mBtnSubmit;

    private WalletModel mWalletModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_putforward;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWalletModel.onDestroy();
    }

    private void init() {
        setTextTitle("提现");
        setvMainBackgroundColor(R.color.bg_content);
        mWalletModel = new WalletModelImpl();
        mWalletModel.onStart(this);
    }

    private void initSetting() {

    }

    @OnClick(R.id.btn_putforward)
    public void onClickSubmit(){
        String money = mEtMoney.getText().toString();
        String bankName = mEtBank.getText().toString();
        String bankNum = mEtNumber.getText().toString();
        String name = mEtName.getText().toString();
        String tel = mEtTel.getText().toString();
        if (TextUtils.isEmpty(money)){
            BaseUtil.makeText("请输入提现金额");
            return;
        }
        if (TextUtils.isEmpty(bankName)){
            BaseUtil.makeText("请输入银行名称");
            return;
        }
        if (TextUtils.isEmpty(bankNum)){
            BaseUtil.makeText("请输入银行卡号");
            return;
        }
        if (TextUtils.isEmpty(name)){
            BaseUtil.makeText("请输入持卡人姓名");
            return;
        }
        if (TextUtils.isEmpty(tel)){
            BaseUtil.makeText("请输入预留号码");
            return;
        }
        mWalletModel.putForward(Const.getUserID(), money, bankName, bankNum, name, tel, new BeanCallback<BaseJson>(this,true,null) {
            @Override
            public void response(boolean success, BaseJson response, int id) {
                if (success){
                    BaseUtil.makeText("提现成功");
                }
            }
        });
    }


}
