package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.View;

import com.jyt.baseapp.R;


public class CallActivity extends BaseMCVActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
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

    private void init(){
        HideActionBar();
        setvMainBackground(R.mipmap.bg_entrance);
    }

    private void initSetting(){

    }

}
