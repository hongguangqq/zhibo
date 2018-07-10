package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FactoryPageAdapter;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.helper.IntentRequestCode;
import com.jyt.baseapp.model.LoginModel;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.LoginModelImpl;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.fragment.FragmentTab1;
import com.jyt.baseapp.view.fragment.FragmentTab2;
import com.jyt.baseapp.view.fragment.FragmentTab3;
import com.jyt.baseapp.view.fragment.FragmentTab4;
import com.jyt.baseapp.view.widget.CircleImageView;
import com.jyt.baseapp.view.widget.NoScrollViewPager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imlib.RongIMClient;

public class ContentActivity extends BaseMCVActivity implements View.OnClickListener {

    @BindView(R.id.vp_content)
    NoScrollViewPager mVpContent;
    @BindView(R.id.iv_tab1)
    ImageView mIvTab1;
    @BindView(R.id.tv_tab1)
    TextView mTvTab1;
    @BindView(R.id.ll_tab1)
    LinearLayout mLlTab1;
    @BindView(R.id.iv_tab2)
    ImageView mIvTab2;
    @BindView(R.id.civ_tab2_hint)
    CircleImageView mCivTab2Hint;
    @BindView(R.id.tv_tab2)
    TextView mTvTab2;
    @BindView(R.id.ll_tab2)
    LinearLayout mLlTab2;
    @BindView(R.id.iv_tab3)
    ImageView mIvTab3;
    @BindView(R.id.tv_tab3)
    TextView mTvTab3;
    @BindView(R.id.ll_tab3)
    LinearLayout mLlTab3;
    @BindView(R.id.iv_tab4)
    ImageView mIvTab4;
    @BindView(R.id.tv_tab4)
    TextView mTvTab4;
    @BindView(R.id.ll_tab4)
    LinearLayout mLlTab4;
    @BindView(R.id.ll_tab_content)
    LinearLayout mLlTabContent;
    @BindView(R.id.iv_tab_add)
    ImageView mIvTabAdd;
    @BindView(R.id.btn_random)
    Button mBtnRandom;
    @BindView(R.id.btn_eavesdrop)
    Button mBtnEavesdrop;

    private  int index_page=1;//page当前的位置
    private static boolean state_add;//add按钮的状态，默认关闭

    private Animation mAnim_in;
    private Animation mAnim_out;
    private List<Fragment> mFragmentList;
    private FragmentViewPagerAdapter mViewPagerAdapter;
    private FactoryPageAdapter mFactoryPageAdapter;
    private PersonModel mPersonModel;
    private LoginModel mLoginModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_content;
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
        initListener();


    }

    private void init() {
        HideActionBar();
        mAnim_in = AnimationUtils.loadAnimation(this,R.anim.button_alpha_in);
        mAnim_out = AnimationUtils.loadAnimation(this,R.anim.button_alpha_out);
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mLoginModel = new LoginModelImpl();
        mLoginModel.onStart(this);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragmentTab1());
        mFragmentList.add(new FragmentTab2());
        mFragmentList.add(new FragmentTab3());
        mFragmentList.add(new FragmentTab4());
        mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        mFactoryPageAdapter = new FactoryPageAdapter(getSupportFragmentManager(),4);
    }



    private void initSetting(){
        //隐藏按钮
        mAnim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnRandom.setVisibility(View.INVISIBLE);
                mBtnEavesdrop.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mViewPagerAdapter.setFragments(mFragmentList);
        mVpContent.setAdapter(mViewPagerAdapter);
        mVpContent.setOffscreenPageLimit(4);
//        mVpContent.setAdapter(mFactoryPageAdapter);
        //小米登录
        MiPushClient.setAlias(this, Const.getUserID(),null);
        //融云登录
        mLoginModel.GetRongID(new BeanCallback<String>() {
            @Override
            public void response(boolean success, String response, int id) {
                try {
                    JSONObject job = new JSONObject(response);
                    String token = job.getString("token");
                    BaseUtil.setSpString(Const.RongToken,token);
                    RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
                        @Override
                        public void onTokenIncorrect() {

                        }

                        @Override
                        public void onSuccess(String s) {
                            BaseUtil.e("onSuccess---id="+s);


                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            BaseUtil.e(errorCode.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //网易云音视频登录
        LoginInfo loginInfo = new LoginInfo(Const.getWyAccount(),Const.getWyToken());
        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object param) {
                Log.e("@#","Wy-onSuccess");
                Log.e("@#","WY-accId："+Const.getWyAccount());
            }

            @Override
            public void onFailed(int code) {
                Log.e("@#","Wy-onFailed-code:"+code);
            }

            @Override
            public void onException(Throwable exception) {
                Log.e("@#","Wy-onException:"+exception.getMessage());
            }
        });


    }

    private void initListener(){
        mIvTabAdd.setOnClickListener(this);
        mBtnRandom.setOnClickListener(this);
        mLlTab1.setOnClickListener(this);
        mLlTab2.setOnClickListener(this);
        mLlTab3.setOnClickListener(this);
        mLlTab4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_tab_add:
                if (!state_add){
                    //关闭->打开
                    mBtnRandom.setVisibility(View.VISIBLE);
                    mBtnEavesdrop.setVisibility(View.VISIBLE);
                    mBtnRandom.startAnimation(mAnim_in);
                    mBtnEavesdrop.startAnimation(mAnim_in);
                    state_add=true;
                }else {
                    //打开->关闭
                    mBtnRandom.startAnimation(mAnim_out);
                    mBtnEavesdrop.startAnimation(mAnim_out);
                    state_add=false;
                }
                break;
            case R.id.btn_random:
                IntentHelper.OpenMateActivity(this);
                break;
            case R.id.ll_tab1:
                ChangeTab(1);
                break;
            case R.id.ll_tab2:
                ChangeTab(2);
                break;
            case R.id.ll_tab3:
                ChangeTab(3);
                break;
            case R.id.ll_tab4:
                ChangeTab(4);
                break;
        }
    }


    private void ChangeTab(int pos){
        if (pos==index_page){
            return;
        }
        mIvTab1.setBackgroundResource(R.mipmap.tab_1_nolx);
        mIvTab2.setBackgroundResource(R.mipmap.tab_2_nolx);
        mIvTab3.setBackgroundResource(R.mipmap.tab_3_nolx);
        mIvTab4.setBackgroundResource(R.mipmap.tab_4_nolx);
        mTvTab1.setTextColor(getResources().getColor(R.color.tab_no));
        mTvTab2.setTextColor(getResources().getColor(R.color.tab_no));
        mTvTab3.setTextColor(getResources().getColor(R.color.tab_no));
        mTvTab4.setTextColor(getResources().getColor(R.color.tab_no));
        switch (pos){
            case 1:
                mIvTab1.setBackgroundResource(R.mipmap.tab_1_selx);
                mTvTab1.setTextColor(getResources().getColor(R.color.tab_sel));
                mVpContent.setCurrentItem(0,false);
                index_page=1;
                break;
            case 2:
                mIvTab2.setBackgroundResource(R.mipmap.tab_2_selx);
                mTvTab2.setTextColor(getResources().getColor(R.color.tab_sel));
                mVpContent.setCurrentItem(1,false);
                index_page=2;
                break;
            case 3:
                mIvTab3.setBackgroundResource(R.mipmap.tab_3_selx);
                mTvTab3.setTextColor(getResources().getColor(R.color.tab_sel));
                mVpContent.setCurrentItem(2,false);
                index_page=3;
                break;
            case 4:
                mIvTab4.setBackgroundResource(R.mipmap.tab_4_selx);
                mTvTab4.setTextColor(getResources().getColor(R.color.tab_sel));
                mVpContent.setCurrentItem(3,false);
                index_page=4;
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentRequestCode.CODE_MODIFY){
            FragmentTab4 fragmentTab4 = (FragmentTab4) mFragmentList.get(3);
            fragmentTab4.setConstData();
        }
    }

    /**
     * 双击退出
     */
    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{//退出程序
            RongIMClient.getInstance().disconnect();
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonModel.onDestroy();
        mLoginModel.onDestroy();
    }
}
