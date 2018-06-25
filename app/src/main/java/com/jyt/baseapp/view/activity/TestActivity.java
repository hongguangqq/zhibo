package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.service.ScannerController;


public class TestActivity extends AppCompatActivity {
    private Button mBtnStar;
    private Button mBtnClose;
    private LinearLayout mll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mBtnStar = findViewById(R.id.btn_star);
        mBtnClose = findViewById(R.id.btn_close);
        mll = findViewById(R.id.ll_render);
        mBtnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerController.getInstance().startMonkServer(TestActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != ScannerController.getInstance().getLocalRender()){
                            mll.addView(ScannerController.getInstance().getLocalRender());
//                            ScannerController.getInstance().createAndJoinRoom();
                            ScannerController.getInstance().show();
                        }

                    }
                }, 1000);

            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScannerController.getInstance().stopMonkServer(TestActivity.this);
    }
}
