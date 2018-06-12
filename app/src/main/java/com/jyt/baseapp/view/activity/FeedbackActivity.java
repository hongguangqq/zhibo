package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.WyManager;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.netease.cloud.nos.android.core.CallRet;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.OnClick;


public class FeedbackActivity extends BaseMCVActivity {

    @BindView(R.id.et_feedback)
    EditText mEtInput;
    @BindView(R.id.rl_feedback_upload)
    RelativeLayout mRlUpload;
    @BindView(R.id.iv_feedback_upload)
    ImageView mIvUpload;
    @BindView(R.id.btn_feedback_submit)
    Button mBtnSubmit;

    private ImagePicker mPicker;
    private File mFilePic;
    private PersonModel mPersonModel;
    private WyManager mWyManager;
    private String mImgPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
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
        setTextTitle("意见反馈");
        setvMainBackgroundColor(R.color.bg_content);
        mPersonModel = new PersonModelImpl();
        mWyManager = WyManager.getManager();
        mPersonModel.onStart(this);
        mPicker = new ImagePicker();
        //是否裁剪图片
        mPicker.setCropImage(false);
    }

    private void initSetting() {

    }

    @OnClick(R.id.btn_feedback_submit)
    public void Submit(){
        String content =mEtInput.getText().toString();
        if (TextUtils.isEmpty(content)){
            BaseUtil.makeText("请填写反馈内容");
            return;
        }
        mPersonModel.FeedBack(mImgPath, content, new BeanCallback<BaseJson>(this,false,null) {
            @Override
            public void response(boolean success, BaseJson response, int id) {
                if (success && response.getCode()==200){
                    BaseUtil.makeText("反馈成功");
                    finish();
                }
            }
        });
    }

    @OnClick(R.id.rl_feedback_upload)
    public void ChoicePic(){
        mPicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override public void onPickImage(Uri imageUri) {
                try {
                    mFilePic = new File(new URI(imageUri.toString()));
                    mIvUpload.setScaleType(ImageView.ScaleType.CENTER);
                    Glide.with(FeedbackActivity.this).load(imageUri).into(mIvUpload);
                    mWyManager.UploadWy(FeedbackActivity.this, mFilePic.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                        @Override
                        public void onUploadContextCreate(Object o, String s, String s1) {

                        }

                        @Override
                        public void onProcess(Object o, long l, long l1) {

                        }

                        @Override
                        public void onSuccess(CallRet callRet) {
                            mImgPath = Const.WyMainFile+mFilePic.getName();
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

            // 裁剪图片回调
            @Override public void onCropImage(Uri imageUri) {


            }

            // 自定义裁剪配置
            @Override public void cropConfig(CropImage.ActivityBuilder builder) {

            }

            // 用户拒绝授权回调
            @Override public void onPermissionDenied(int requestCode, String[] permissions,
                                                     int[] grantResults) {
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFilePic!=null){
            mFilePic.delete();
        }
        mPersonModel.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
