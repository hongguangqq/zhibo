package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LinWei on 2018/5/14 15:30
 */
public class RecordDialog extends AlertDialog {


    @BindView(R.id.tv_record_title)
    TextView mTvTitle;

    public RecordDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_record);
        ButterKnife.bind(this);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.horizontalMargin = 0;//设置水平方向的Margin
        params.verticalMargin = 0;//设置竖直方向的Margin
        params.dimAmount = 0.5f;//背景黑暗度
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;//设置宽度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//设置高度
        params.gravity = Gravity.CENTER;
        getWindow().setBackgroundDrawable(null);
        getWindow().setGravity(Gravity.CENTER);//对齐
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(params);//加入设置
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//背景黑暗

    }


    public void setTitle(String title){
        mTvTitle.setText(title);
    }


}
