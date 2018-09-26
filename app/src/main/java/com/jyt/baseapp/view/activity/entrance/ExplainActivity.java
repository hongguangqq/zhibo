package com.jyt.baseapp.view.activity.entrance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.view.activity.BaseMCVActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ExplainActivity extends BaseMCVActivity {

    @BindView(R.id.rl_explain_b1)
    RelativeLayout mRlB1;
    @BindView(R.id.rl_explain_b2)
    RelativeLayout mRlB2;
    @BindView(R.id.rl_explain_b3)
    RelativeLayout mRlB3;
    @BindView(R.id.rl_explain_b4)
    RelativeLayout mRlB4;
    @BindView(R.id.rl_explain_b5)
    RelativeLayout mRlB5;
    @BindView(R.id.rl_explain_b6)
    RelativeLayout mRlB6;
    @BindView(R.id.btn_explain_next)
    Button mBtnNext;

    private int rid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_explain;
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
        setTextTitle("女用户特别说明");
        setvActionBarColor(R.color.bg_content);
        Tuple tuple =IntentHelper.ExplainActivityGetPara(getIntent());
        rid = (int) tuple.getItem1();
    }

    private void initSetting() {

    }

    @OnClick(R.id.btn_explain_next)
    public void OpenAuthenticationActivity() {
        IntentHelper.OpenAuthenticationActivity(ExplainActivity.this,rid,0);
    }

    @OnClick(R.id.rl_explain_b1)
    public void onClickB1(){
        IntentHelper.openServiceWebActivity(this,"10001");
    }

    @OnClick(R.id.rl_explain_b2)
    public void onClickB2(){
        IntentHelper.openServiceWebActivity(this,"10002");
    }

    @OnClick(R.id.rl_explain_b3)
    public void onClickB3(){
        IntentHelper.openServiceWebActivity(this,"10003");
    }

    @OnClick(R.id.rl_explain_b4)
    public void onClickB4(){
        IntentHelper.openServiceWebActivity(this,"10004");
    }

    @OnClick(R.id.rl_explain_b5)
    public void onClickB5(){
        IntentHelper.openServiceWebActivity(this,"10005");
    }

    @OnClick(R.id.rl_explain_b6)
    public void onClickB6(){
        IntentHelper.openServiceWebActivity(this,"10006");
    }

//    /**
//     * 双击退出
//     */
//    private long mPressedTime = 0;
//
//    @Override
//    public void onBackPressed() {
//        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
//        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
//            Toast.makeText(this, "再按一次返回登录界面", Toast.LENGTH_SHORT).show();
//            mPressedTime = mNowTime;
//        } else {//退出程序
//            this.finish();
//            System.exit(0);
//        }
//    }


}





