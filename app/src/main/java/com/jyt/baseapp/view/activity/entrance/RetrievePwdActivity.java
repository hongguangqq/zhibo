package com.jyt.baseapp.view.activity.entrance;

import android.os.Bundle;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.view.activity.BaseMCVActivity;

public class RetrievePwdActivity extends BaseMCVActivity {

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
        setTextTitle("找回密码");
        setFunctionText("提交");
        showFunctionImage();
        setvMainBackground(R.mipmap.bg_entrance);
    }
}
