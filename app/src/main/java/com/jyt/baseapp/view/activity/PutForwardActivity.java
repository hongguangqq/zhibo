package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jyt.baseapp.R;

import butterknife.BindView;


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

    private void init() {
        setTextTitle("提现");
        setvMainBackgroundColor(R.color.bg_content);
    }

    private void initSetting() {

    }


}
