package com.jyt.baseapp.view.activity.entrance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.AuthModel;
import com.jyt.baseapp.model.impl.AuthModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.WyManager;
import com.jyt.baseapp.view.activity.BaseMCVActivity;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.netease.cloud.nos.android.core.CallRet;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthenticationActivity extends BaseMCVActivity {
    @BindView(R.id.et_at_name)
    EditText mEtName;
    @BindView(R.id.et_at_card)
    EditText mEtCard;
    @BindView(R.id.fl_at_photo)
    FrameLayout mFlPhoto;
    @BindView(R.id.iv_at_photo)
    ImageView mIvPhoto;
    private WyManager mWyManager;
    private boolean canNext;
    private int rid;
    private ImagePicker mPicker;
    private File mCardFile;
    private AuthModel mAuthModel;
    private boolean isAuthing;
    private int code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authentication;
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
        setTextTitle("实名认证");
        setFunctionText("提交认证");
        mWyManager = WyManager.getManager();
        showFunctionImage();
        setvMainBackground(R.mipmap.bg_entrance);
        Tuple tuple = IntentHelper.AuthenticationActivityGetPara(getIntent());
        rid = (int) tuple.getItem1();
        code = (int) tuple.getItem2();
        mAuthModel = new AuthModelImpl();
    }

    private void initSetting(){
        mPicker = new ImagePicker();
        //是否裁剪图片
        mPicker.setCropImage(false);

        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {
                String name = mEtName.getText().toString();
                String infoCard = mEtCard.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    BaseUtil.makeText("姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(infoCard)) {
                    BaseUtil.makeText("身份证号码不能为空");
                    return;
                }
                if (mCardFile ==null){
                    BaseUtil.makeText("请上传手持身份证");
                    return;
                }
                if (canNext){
                    return;
                }
                canNext=true;
                mAuthModel.AuthInfo(rid,name, infoCard, Const.WyMainFile + mCardFile.getName(), new BeanCallback<BaseJson>(AuthenticationActivity.this,true,null) {
                    @Override
                    public void response(boolean success, BaseJson response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                BaseUtil.makeText("验证成功");
                                if (code==0){
                                    IntentHelper.OpenLoginActivityByAuth(AuthenticationActivity.this);
                                }else {
                                    finish();
                                }
                            } else if (response.getCode()==500){
                                BaseUtil.makeText(response.getMessage());
                            }
                        }
                        canNext=false;
                    }
                });

            }
        });
    }

    @OnClick(R.id.fl_at_photo)
    public void ChoicePhoto(){
        mPicker.startChooser(this, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {
                try {
                    mCardFile = new File(new URI(imageUri.toString()));
                    mIvPhoto.setVisibility(View.VISIBLE);
                    mIvPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(AuthenticationActivity.this).load(imageUri).into(mIvPhoto);
                    mWyManager.UploadWy(AuthenticationActivity.this, mCardFile.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                        @Override
                        public void onUploadContextCreate(Object o, String s, String s1) {

                        }

                        @Override
                        public void onProcess(Object o, long l, long l1) {

                        }

                        @Override
                        public void onSuccess(CallRet callRet) {

                        }

                        @Override
                        public void onFailure(CallRet callRet) {

                        }

                        @Override
                        public void onCanceled(CallRet callRet) {

                        }
                    });
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPicker.onActivityResult(this,requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCardFile !=null){
            mCardFile.delete();
        }
    }
}
