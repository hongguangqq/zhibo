package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择支付对话框
 * Created by chenweiqi on 2017/6/2.
 */

public class PayDialog extends AlertDialog {

    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.cb_wxpay)
    CheckBox cbWxpay;
    @BindView(R.id.layout_wxpay)
    RelativeLayout layoutWxpay;
    @BindView(R.id.cb_alipay)
    CheckBox cbAlipay;
    @BindView(R.id.layout_alipay)
    RelativeLayout layoutAlipay;
    @BindView(R.id.btn_done)
    TextView btnDone;


    OnClickListener onClickListener;

    public PayDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_pay);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_close)
    public void onBtnDoneClick(){
        dismiss();
    }

    @OnClick({R.id.layout_wxpay, R.id.layout_alipay})
    public void onItemClick(View view){
        cbAlipay.setChecked(false);
        cbWxpay.setChecked(false);
        switch (view.getId()){
            case R.id.layout_wxpay:
                cbWxpay.setChecked(true);
                break;
            case R.id.layout_alipay:
                cbAlipay.setChecked(true);
                break;
        }
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        if (onClickListener!=null){
            if (cbWxpay.isChecked()) {
                onClickListener.onClick(this,1);
            }else if (cbAlipay.isChecked()){
                onClickListener.onClick(this,2);
            }else {
                T.showShort(getContext(),"请选择支付方式");
            }
        }
    }

    public void setOnDoneClickListener(OnClickListener onDoneClickListener){
        this.onClickListener = onDoneClickListener;
    }

}
