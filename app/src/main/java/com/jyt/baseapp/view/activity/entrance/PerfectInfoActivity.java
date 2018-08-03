package com.jyt.baseapp.view.activity.entrance;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.model.impl.RegisterModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.FinishActivityManager;
import com.jyt.baseapp.view.activity.BaseMCVActivity;
import com.jyt.baseapp.view.widget.CityPickerView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import butterknife.BindView;
import butterknife.OnClick;

public class PerfectInfoActivity extends BaseMCVActivity  {

    @BindView(R.id.et_pi_nick)
    EditText mEtNick;
    @BindView(R.id.tv_pi_birthday)
    TextView mTvBirthday;
    @BindView(R.id.ll_pi_birthday)
    LinearLayout mLlBirthday;
    @BindView(R.id.tv_pi_city)
    TextView mTvCity;
    @BindView(R.id.ll_pi_city)
    LinearLayout mLlCity;
    @BindView(R.id.tv_pi_sex)
    TextView mTvSex;
    @BindView(R.id.iv_pi_female)
    ImageView mIvFemale;
    @BindView(R.id.iv_pi_male)
    ImageView mIvMale;

    private int mSexcode;
    private String mTel;
    private String mIode;
    private String mPwd;
    private String mVode;
    private boolean mIsQW;
    private String mQWId;
    private String mName;
    private String mBirthday;
    private String mCity;
    private boolean mQOW;
    private TimePickerDialog mTimePicker;
    private CityPickerView mCityOptions;
    private RegisterModel mRegisterModel;
    private boolean isRegistering;
    private boolean isMatch;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_perfect_info;
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
        setTextTitle("完善信息");
        setFunctionText("提交");
        showFunctionImage();
        setvMainBackground(R.mipmap.bg_entrance);
        mRegisterModel = new RegisterModelImpl();
        mRegisterModel.onStart(this);
        Tuple tuple = IntentHelper.PerfectInfoActivityGetPara(getIntent());

        mIsQW = (boolean) tuple.getItem1();
        if (mIsQW){
            mQWId = (String) tuple.getItem2();
            mQOW = (boolean) tuple.getItem3();
            Log.e("@#","QW="+mQWId);
        }else {
            mTel = (String) tuple.getItem2();
            mIode = (String) tuple.getItem3();
            mPwd = (String) tuple.getItme4();
            mVode = (String) tuple.getItem5();
        }
    }

    private void initSetting(){
        mTimePicker = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        mBirthday = BaseUtil.TransTime(millseconds,"yyyy-MM-dd");
                        mTvBirthday.setText(mBirthday);

                    }
                })
                .setTitleStringId("生日")
                .setSureStringId("确定")
                .setCancelStringId("取消")
                .setCyclic(false)
                .setMinMillseconds(33804000)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis())
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextSize(18)
                .build();
        mCityOptions = new CityPickerView(this,new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Log.e("@#","city="+mCityOptions.getCity(options1,options2));
                mTvCity.setText(mCityOptions.getCity(options1,options2));
                mCity = mCityOptions.getCity(options1,options2);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true

                .setOutSideCancelable(true)//点击外部dismiss default true
        ,true);
        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {
                mName = mEtNick.getText().toString();
                if (TextUtils.isEmpty(mName)){
                    BaseUtil.makeText("昵称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mBirthday)){
                    BaseUtil.makeText("生日不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mCity)){
                    BaseUtil.makeText("城市不能为空");
                    return;
                }
                if (mSexcode==0){
                    BaseUtil.makeText("请选择您的性别");
                    return;
                }

                if (mIsQW){
                    //第三方登录
                    if (mSexcode==1){
                        //男用户
                        finish();
                    }else if (mSexcode==2){
                        //女用户
                        IntentHelper.OpenBindTelActivity(PerfectInfoActivity.this,mQWId,mName,mBirthday,mCity,mSexcode,mQOW);
                    }
                }else {
                    //电话号码登录
                    if (isRegistering){
                        return;
                    }
                    isRegistering=true;
                    mRegisterModel.Register(mTel, mVode, mPwd, mIode, mName, mBirthday, mCity, mSexcode, new BeanCallback<BaseJson<UserBean>>(PerfectInfoActivity.this,false,null) {
                        @Override
                        public void response(boolean success, BaseJson<UserBean> response, int id) {
                            BaseUtil.e("response="+response+"-----"+success);
                            isRegistering=false;
                            if (success){
                                if (response.getCode()==200){
                                    Log.e("@#","success");
                                    if (mSexcode==1){
                                        //男用户注册成功，返回登录界面
                                        BaseUtil.makeText("注册成功");
                                        FinishActivityManager.getManager().finishActivity(RegisterActivity.class);
                                        finish();
                                    }else if (mSexcode==2){
                                        //女用户注册成功，进入说明界面
                                        BaseUtil.makeText("注册成功,请完善您的资料");
                                        BaseUtil.e("id="+response.getData().getId());
                                        IntentHelper.OpenExplainActivity(PerfectInfoActivity.this,response.getData().getId()+"");
                                    }
                                }else if (response.getCode()==500){
                                    BaseUtil.makeText(response.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });
    }



    private void ChoiceSex(int sexCode){
        mIvFemale.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nv));
        mIvMale.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nan));
        mSexcode = sexCode;
        if (sexCode==1){
            mIvMale.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nan_sel));
            mTvSex.setText("男");
        }else if (sexCode==2){
            mIvFemale.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nv_sel));
            mTvSex.setText("女");
        }
    }

    @OnClick(R.id.iv_pi_female)
    public void ChoiceSexByFemale(){
        ChoiceSex(2);
    }

    @OnClick(R.id.iv_pi_male)
    public void ChoiceSexByMale(){
        ChoiceSex(1);
    }

    @OnClick(R.id.ll_pi_birthday)
    public void ChoiceBirthday(){
        mTimePicker.show(getSupportFragmentManager(),"");
    }
    @OnClick(R.id.ll_pi_city)
    public void ChoiceCity(){
        mCityOptions.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegisterModel.onDestroy();
    }
}
