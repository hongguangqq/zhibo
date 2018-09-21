package com.jyt.baseapp.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.helper.IntentRequestCode;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.WyManager;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.dialog.LoadingDialog;
import com.jyt.baseapp.view.dialog.RecordDialog;
import com.jyt.baseapp.view.widget.CircleImageView;
import com.jyt.baseapp.view.widget.CityPickerView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.netease.cloud.nos.android.core.CallRet;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

import static com.jyt.baseapp.api.Const.PERMISSIONS_REQUEST_FOR_AUDIO;


public class ModifyActivity extends BaseMCVActivity {

    @BindView(R.id.iv_info_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.iv_info_sex)
    ImageView mIvSex;
    @BindView(R.id.tv_info_name)
    TextView mTvName;
    @BindView(R.id.ll_info_name)
    LinearLayout mLlName;
    @BindView(R.id.tv_info_birthday)
    TextView mTvBirthday;
    @BindView(R.id.ll_info_birthday)
    LinearLayout mLlBirthday;
    @BindView(R.id.tv_info_city)
    TextView mTvCity;
    @BindView(R.id.ll_info_city)
    LinearLayout mLlCity;
    @BindView(R.id.tv_info_work)
    TextView mTvWork;
    @BindView(R.id.ll_info_work)
    LinearLayout mLlWork;
    @BindView(R.id.tv_info_sign)
    TextView mTvSign;
    @BindView(R.id.tv_info_mark)
    TextView mTvMark;
    @BindView(R.id.iv_info_pic)
    ImageView mIvPic;
    @BindView(R.id.ll_info_pic)
    LinearLayout mLlPic;
    @BindView(R.id.rl_info_video)
    RelativeLayout mRlVideo;
    @BindView(R.id.iv_info_video1)
    ImageView mIvVideo1;
    @BindView(R.id.iv_info_video2)
    ImageView mIvVideo2;
    @BindView(R.id.iv_info_sound1)
    ImageView mIvSound1;
    @BindView(R.id.iv_info_sound2)
    ImageView mIvSound2;

    private PersonModel mPersonModel;
    private IPhoneDialog mCodeDialog;
    private TimePickerDialog mTimePicker;
    private CityPickerView mCityOptions;
    private int mCodeState;
    private OnImageClickListener mClickListener;
    private OnImageLongClickListener mOnImageLongClickListener;
    private ImagePicker PickerHead;//用于头像上传
    private ImagePicker PickerUpload;//用于宣传照上传
    private File mFileHPic;//用于切换头像时短暂的本地保存，上传后删除
    private File mFilePP;//用于宣传照短暂的本地保存，上传后删除
    private File mFileVideo;//用于视频短暂的本地保存，上传后删除
    private File mFileFeng;//视频封面
    private LinearLayout.LayoutParams mImgParams;
    private boolean isHpic;//上传的图片是否为头像
    private LoadingDialog mLoadingDialog;
    private WyManager mWyManager;
    private UserBean mUserData;

    /*录音*/
    private RecordDialog mRecordDialog;
    /*播放录音*/
    private RecordDialog mPlayDialog;
    //线程操作
    private ExecutorService mExecutorService;
    //录音API
    private MediaRecorder mMediaRecorder;
    //录音开始时间与结束时间
    private long startTime, endTime;
    //录音所保存的文件
    private File mAudioFile;
    //当前是否正在播放
    private boolean isPlayAudio;
    //播放音频文件API
    private MediaPlayer mediaPlayer;
    //使用Handler更新UI线程
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //录音成功
                case Const.RECORD_SUCCESS:
                    mWyManager.UploadWy(ModifyActivity.this, mAudioFile.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                        @Override
                        public void onUploadContextCreate(Object o, String s, String s1) {
                            BaseUtil.makeText("录音成功");
                            //上传文件到云
                            mWyManager.UploadWy(ModifyActivity.this, mAudioFile.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                                @Override
                                public void onUploadContextCreate(Object o, String s, String s1) {

                                }

                                @Override
                                public void onProcess(Object o, long l, long l1) {

                                }

                                @Override
                                public void onSuccess(CallRet callRet) {
                                    mIvSound2.setVisibility(View.VISIBLE);
                                    mUserData.setVoice(Const.WyMainFile+mAudioFile.getName());
                                    UpDateMyData();
                                }

                                @Override
                                public void onFailure(CallRet callRet) {
                                    BaseUtil.makeText("上传失败,请重试");
                                }

                                @Override
                                public void onCanceled(CallRet callRet) {

                                }
                            });

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
                    break;
                //录音失败
                case Const.RECORD_FAIL:
                    BaseUtil.makeText("录音失败");
                    break;
                //录音时间太短
                case Const.RECORD_TOO_SHORT:
                    BaseUtil.makeText("录音时间太短");
                    break;
                case Const.PLAY_COMPLETION:

                    break;
                case Const.PLAY_ERROR:

                    break;

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify;
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
        setTextTitle("个人信息");

        setvMainBackgroundColor(R.color.bg_content);
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mLoadingDialog = new LoadingDialog(this);
        mWyManager = WyManager.getManager();
        mExecutorService = Executors.newSingleThreadExecutor();
        mRecordDialog = new RecordDialog(this);
        mPlayDialog = new RecordDialog(this);
        mClickListener = new OnImageClickListener();
        mOnImageLongClickListener = new OnImageLongClickListener();
        PickerHead = new ImagePicker();
        PickerUpload = new ImagePicker();
        mImgParams = new LinearLayout.LayoutParams(BaseUtil.dip2px(63),BaseUtil.dip2px(63));
        mCodeDialog = new IPhoneDialog(this);
        mCodeDialog.setInputShow(true);
        mCodeDialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                if (input ==null){
                    input = "";
                }
                switch (mCodeState){
                    case 1:
                        if (TextUtils.isEmpty(input)){
                            BaseUtil.makeText("昵称不能为空");
                            return;
                        }
                        mUserData.setNickname(input);
                        break;
                    case 2:
                        mUserData.setProfession(input);
                        break;
                    case 3:
                        mUserData.setIntroduction(input);
                        break;
                    case 4:
                        mUserData.setMark(input);
                        break;
                }
                UpDateMyData();
            }

            @Override
            public void ClickCancel() {

            }
        });
    }

    private void initSetting() {
        mImgParams.setMargins(BaseUtil.dip2px(13),0,0,0);
        // 设置标题
        PickerHead.setTitle("裁剪");
        // 设置是否裁剪图片
        PickerHead.setCropImage(true);
        PickerUpload.setCropImage(false);
        mPersonModel.getMyUserData(new BeanCallback<BaseJson<UserBean>>(this,false,null) {
            @Override
            public void response(boolean success, BaseJson<UserBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        mUserData = response.getData();
                        Log.e("@#",mUserData.toString());
                        setMyUserData();
                    }
                }
            }
        });
        mTimePicker = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        mUserData.setBirthday(BaseUtil.TransTime(millseconds,"yyyy-MM-dd"));
                        UpDateMyData();

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
                mUserData.setCityName(mCityOptions.getCity(options1,options2));
                UpDateMyData();
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
                IntentHelper.OpenAuthenticationActivity(ModifyActivity.this,Integer.parseInt(Const.getUserID()),1);
            }
        });
    }

    private void UpDateMyData(){
        mPersonModel.UpDateMyData(mUserData, new BeanCallback<BaseJson<UserBean>>(this,false,null) {
            @Override
            public void response(boolean success, BaseJson<UserBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        mUserData = response.getData();
                        setMyUserData();
                    }
                }
            }
        });
    }

    private void setMyUserData(){
        switch (mUserData.getAnchorState()){
            case 0:
            case 3:
                setFunctionText("主播认证");
                showFunctionText();
                break;
            case 1:
            case 2:
                hideFunctionText();

        }
        Glide.with(BaseUtil.getContext()).load(mUserData.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
        if (mUserData.getGender()==2){
            mIvSex.setImageDrawable(getResources().getDrawable(R.mipmap.icon_nv2));
        }
        mTvName.setText(mUserData.getNickname());
        mTvBirthday.setText(mUserData.getBirthday());
        mTvCity.setText(mUserData.getCityName());
        if (mUserData.getProfession()==null){
            mTvWork.setHint("未设置");
        }else {
            mTvWork.setText(mUserData.getProfession());
        }
        if (mUserData.getBirthday()!=null){
            mTvBirthday.setText(mUserData.getBirthday());
        }
        if (mUserData.getCityName()!=null){
            mTvCity.setText(mUserData.getCityName());
        }
        if (mUserData.getIntroduction()!=null){
            mTvSign.setText(mUserData.getIntroduction());
        }
        if (mUserData.getMark()!=null){
            mTvMark.setText(mUserData.getMark());
        }
        //宣传视频
        if (!TextUtils.isEmpty(mUserData.getVideo())){
            mRlVideo.setVisibility(View.VISIBLE);
            mIvVideo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this).load(mUserData.getVedioImg()).into(mIvVideo2);
        }else {
            mRlVideo.setVisibility(View.GONE);
        }
        //宣传照片
        mLlPic.removeAllViews();//清除所有的子项
        if (mUserData.getImgsArray()!=null){
            for (int i = 0; i < mUserData.getImgsArray().size(); i++) {
                ImageView iv = new ImageView(ModifyActivity.this);
                iv.setLayoutParams(mImgParams);
                iv.setOnClickListener(mClickListener);
                iv.setOnLongClickListener(mOnImageLongClickListener);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(ModifyActivity.this).load(mUserData.getImgsArray().get(i)).into(iv);
                iv.setTag(mUserData.getImgsArray().get(i));
                mLlPic.addView(iv);
            }
        }
        //语音
        if (!TextUtils.isEmpty(mUserData.getVoice())){
            mIvSound2.setVisibility(View.VISIBLE);
        }else {
            mIvSound2.setVisibility(View.GONE);
        }


    }



    @OnClick(R.id.iv_info_hpic)
    public void UploadHeadPic(){
        isHpic=true;
        PickerHead.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override public void onCropImage(Uri imageUri) {
                try {
                    mFileHPic = new File(new URI(imageUri.toString()));
                    mWyManager.UploadWy(ModifyActivity.this, mFileHPic.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                        @Override
                        public void onUploadContextCreate(Object o, String s, String s1) {

                        }

                        @Override
                        public void onProcess(Object o, long l, long l1) {

                        }

                        @Override
                        public void onSuccess(CallRet callRet) {
                            mUserData.setHeadImg(Const.WyMainFile+mFileHPic.getName());
                            UpDateMyData();
                        }

                        @Override
                        public void onFailure(CallRet callRet) {
                            BaseUtil.makeText("上传失败,请重试");
                        }

                        @Override
                        public void onCanceled(CallRet callRet) {

                        }
                    });

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }

            // 自定义裁剪配置
            @Override public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.OVAL)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(640, 640);

            }

            // 用户拒绝授权回调
            @Override public void onPermissionDenied(int requestCode, String[] permissions,
                                                     int[] grantResults) {
            }
        });

    }

    @OnClick(R.id.iv_info_pic)
    public void UploadPic(){
        if (mUserData.getImgsArray().size()>=5){
            BaseUtil.makeText("超出最大上传数量");
            return;
        }
        isHpic=false;
        PickerUpload.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override public void onPickImage(Uri imageUri) {
                try {
                    File originalFile = new File(new URI(imageUri.toString()));
                    long fileS = originalFile.length();
                    if ((fileS/1048576)>4){
                        BaseUtil.makeText("图片大小不能超过4MB");
                        return;
                    }
                    //提交到云
                    compressLuban(ModifyActivity.this, originalFile, new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            if (mFilePP!=null && mFilePP.exists()){
                                mFilePP.delete();
                            }
                            mFilePP =file;
                            mWyManager.UploadWy(ModifyActivity.this, mFilePP.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                                @Override
                                public void onUploadContextCreate(Object o, String s, String s1) {

                                }

                                @Override
                                public void onProcess(Object o, long l, long l1) {

                                }

                                @Override
                                public void onSuccess(CallRet callRet) {
                                    //添加新数据
                                    mUserData.getImgsList().add(Const.WyMainFile+mFilePP.getName());
                                    //刷新数据
                                    mUserData.setImgs(new Gson().toJson(mUserData.getImgsList()));
                                    UpDateMyData();
                                }

                                @Override
                                public void onFailure(CallRet callRet) {
                                    Log.e("@#","fail:"+callRet.getException());
                                    BaseUtil.makeText("上传失败,请重试");
                                }

                                @Override
                                public void onCanceled(CallRet callRet) {

                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {

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

    @OnClick(R.id.iv_info_video1)
    public void UploadVideo(){
        if (TextUtils.isEmpty(mUserData.getVideo())){
            IntentHelper.OpenRecordingActivity(this);
        }else {
            BaseUtil.makeText("只能上传一段宣传视频");
        }
    }



    //跳转播放
    @OnClick(R.id.rl_info_video)
    public void OpenVideoActivity(){
        if (!TextUtils.isEmpty(mUserData.getVideo())){
            IntentHelper.OpenVideoActivity(this, mUserData.getVideo());
        }

    }

    @OnLongClick(R.id.rl_info_video)
    public boolean DeleteVideo(){
        IPhoneDialog dialog = new IPhoneDialog(this);
        dialog.setTitle("确认删除?");
        dialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                mRlVideo.setVisibility(View.GONE);
                mUserData.setVedioImg("");
                mUserData.setVideo("");
                UpDateMyData();
            }

            @Override
            public void ClickCancel() {

            }
        });
        dialog.show();
        return false;
    }



    @OnTouch(R.id.iv_info_sound1)
    public boolean UploadSound(MotionEvent event){
        switch (event.getAction()) {
            //按下操作
            case MotionEvent.ACTION_DOWN:
                mRecordDialog.show();
                //安卓6.0以上录音相应权限处理
                if (Build.VERSION.SDK_INT > 22) {
                    permissionForM();
                } else {
                    startRecord();
                }
                Log.e("@#","录音中");
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mRecordDialog.dismiss();
                stopRecord();
                Log.e("@#","录音结束");
                break;
        }
        return true;
    }

    @OnClick(R.id.iv_info_sound2)
    public void PlayAudio(){
        if (!isPlayAudio){
            startPlay(mUserData.getVoice());
        }

    }

    @OnLongClick(R.id.iv_info_sound2)
    public boolean DeleteVoice(){
        IPhoneDialog dialog = new IPhoneDialog(this);
        dialog.setTitle("确认删除?");
        dialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                mIvSound2.setVisibility(View.GONE);
                mUserData.setVoice("");
                UpDateMyData();
            }

            @Override
            public void ClickCancel() {

            }
        });
        dialog.show();
        return false;
    }

    @OnClick(R.id.ll_info_name)
    public void setNickName(){
        setDialogState(1);
    }

    @OnClick(R.id.ll_info_work)
    public void setJob(){
        setDialogState(2);
    }

    @OnClick(R.id.tv_info_sign)
    public void setSign(){
        setDialogState(3);
    }

    @OnClick(R.id.tv_info_mark)
    public void setMark(){
        setDialogState(4);
    }

    @OnClick(R.id.ll_info_birthday)
    public void setBirthday(){
        mTimePicker.show(getSupportFragmentManager(),"");
    }

    @OnClick(R.id.ll_info_city)
    public void setCity(){
        mCityOptions.show();
    }

    private void setDialogState(int code){
        mCodeState = code;
        mCodeDialog.setInputText("");
        switch (code){
            case 1:
                mCodeDialog.setTitle("昵称");
                mCodeDialog.setInputLine(1);
                mCodeDialog.setInputLength(10);
                break;
            case 2:
                mCodeDialog.setTitle("职业");
                mCodeDialog.setInputLine(1);
                mCodeDialog.setInputLength(10);
                break;
            case 3:
                mCodeDialog.setTitle("签名");
                mCodeDialog.setInputLine(4);
                mCodeDialog.setInputLength(60);
                break;
            case 4:
                mCodeDialog.setTitle("备注");
                mCodeDialog.setInputLine(4);
                mCodeDialog.setInputLength(60);
                break;
        }
        mCodeDialog.show();
    }

    private void compressLuban(Context context,File file,OnCompressListener listener){
        Luban.with(context)
                .load(file)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .setFocusAlpha(false)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        try {
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(filePath.getBytes());
                            String suffix  = filePath.substring(filePath.lastIndexOf(".") + 1);
                            return new BigInteger(1, md.digest()).toString(32)+"."+suffix;
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }
                })
                .setCompressListener(listener).launch();
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IntentRequestCode.CODE_RECORDING:
                Tuple result = IntentHelper.RecordingActivityGetPara(data);
                if (result==null){
                    return;
                }
                mRlVideo.setVisibility(View.VISIBLE);
                //视频地址
                mFileVideo = new File((String) result.getItem1());
                mWyManager.UploadWy(ModifyActivity.this, mFileVideo.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                    @Override
                    public void onUploadContextCreate(Object o, String s, String s1) {

                    }

                    @Override
                    public void onProcess(Object o, long l, long l1) {

                    }

                    @Override
                    public void onSuccess(CallRet callRet) {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(mFileVideo.getAbsolutePath());
                        Bitmap bitmap = media.getFrameAtTime();
                        mFileFeng = BaseUtil.saveBitmap(ModifyActivity.this,bitmap);
                        mUserData.setVideo(Const.WyMainFile+mFileVideo.getName());
                        mWyManager.UploadWy(ModifyActivity.this, mFileFeng.getAbsolutePath(), new WyManager.OnWyUploadListener() {
                            @Override
                            public void onUploadContextCreate(Object o, String s, String s1) {

                            }

                            @Override
                            public void onProcess(Object o, long l, long l1) {

                            }

                            @Override
                            public void onSuccess(CallRet callRet) {
                                mUserData.setVedioImg(Const.WyMainFile+mFileFeng.getName());
                                UpDateMyData();
                            }

                            @Override
                            public void onFailure(CallRet callRet) {

                            }

                            @Override
                            public void onCanceled(CallRet callRet) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(CallRet callRet) {

                    }

                    @Override
                    public void onCanceled(CallRet callRet) {

                    }
                });

                break;
            default:
                if (isHpic){
                    PickerHead.onActivityResult(this, requestCode, resultCode, data);
                }else {
                    PickerUpload.onActivityResult(this, requestCode, resultCode, data);
                }
                break;
        }

    }


    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            //录音
            case Const.PERMISSIONS_REQUEST_FOR_AUDIO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecord();
                }
                break;
            default:
                if (isHpic){
                    PickerHead.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
                }else {
                    PickerUpload.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
                }
                break;
        }

    }

    /**
     * @description 开始进行录音
     * @author ldm
     * @time 2017/2/9 9:18
     */
    private void startRecord() {
        //异步任务执行录音操作
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //播放前释放资源
                releaseRecorder();
                //执行录音操作
                recordOperation();
            }
        });
    }

    /**
     * @description 结束录音操作
     * @author ldm
     * @time 2017/2/9 9:18
     */
    private void stopRecord() {
        if (mMediaRecorder!=null){
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
                mAudioFile.delete();
                releaseRecorder();
                mHandler.sendEmptyMessage(Const.RECORD_TOO_SHORT);
                return;
            }
            //记录停止时间
            endTime = System.currentTimeMillis();
            //录音时间处理，比如只有大于2秒的录音才算成功
            int time = (int) ((endTime - startTime) / 1000);
            if (time >= 3) {
                //录音成功,发Message
                mHandler.sendEmptyMessage(Const.RECORD_SUCCESS);
            } else if (time<30){
                mAudioFile = null;
                mHandler.sendEmptyMessage(Const.RECORD_TOO_SHORT);
            }else {
                BaseUtil.makeText("录音不能超过30秒");
            }
            //录音完成释放资源
            releaseRecorder();
        }
    }

    /**
     * @description 翻放录音相关资源
     * @author ldm
     * @time 2017/2/9 9:33
     */
    private void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * @description 录音操作
     * @author ldm
     * @time 2017/2/9 9:34
     */
    private void recordOperation() {
        //创建MediaRecorder对象
        mMediaRecorder = new MediaRecorder();
        //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        mAudioFile = new File(Const.mMainFile, System.currentTimeMillis() + ".m4a");
        //创建父文件夹
        mAudioFile.getParentFile().mkdirs();
        try {
            //创建文件
            mAudioFile.createNewFile();
            //配置mMediaRecorder相应参数
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
            mMediaRecorder.setAudioSamplingRate(44100);
            //设置声音数据编码格式,音频通用格式是AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置编码频率
            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置录音保存的文件
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //记录开始录音时间
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            recordFail();
        }
    }

    /**
     * @description 录音失败处理
     * @author ldm
     * @time 2017/2/9 9:35
     */
    private void recordFail() {
        mAudioFile = null;
        mHandler.sendEmptyMessage(Const.RECORD_FAIL);
    }

    /**
     * @description 开始播放音频文件
     * @author ldm
     * @time 2017/2/9 16:56
     */
    private void startPlay(String path) {
        isPlayAudio=true;
        mPlayDialog.show();
        mPlayDialog.setTitle("播放中");
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(path);
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }

    }

    /**
     * @description 停止播放或播放失败处理
     * @author ldm
     * @time 2017/2/9 16:58
     */
    private void playEndOrFail(boolean isEnd) {
        isPlayAudio = false;
        mPlayDialog.dismiss();
        if (isEnd) {
            mHandler.sendEmptyMessage(Const.PLAY_COMPLETION);
        } else {
            mHandler.sendEmptyMessage(Const.PLAY_ERROR);
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    /*******6.0以上版本手机权限处理***************************/
    /**
     * @description 兼容手机6.0权限管理
     * @author ldm
     * @time 2016/5/24 14:59
     */
    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_FOR_AUDIO);
        } else {
            startRecord();
        }
    }







    class OnImageClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            IntentHelper.openBrowseImagesActivity(ModifyActivity.this, (String) v.getTag());
        }
    }

    class OnImageLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(final View v) {
            IPhoneDialog dialog = new IPhoneDialog(ModifyActivity.this);
            dialog.setTitle("确定删除?");
            dialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
                @Override
                public void ClickSubmit(boolean isShow, String input) {
                    if (mUserData.getImgsList().contains(v.getTag())){
                        mUserData.getImgsList().remove(v.getTag());
                        mUserData.setImgs(new Gson().toJson(mUserData.getImgsList()));
                        UpDateMyData();
                    }
                }

                @Override
                public void ClickCancel() {

                }
            });
            dialog.show();
            return false;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdownNow();
        mPersonModel.onDestroy();
        if (mFileHPic!=null){
            mFileHPic.delete();
        }
        if (mFilePP!=null){
            mFilePP.delete();
        }
        if (mFileVideo !=null){
            mFileVideo.delete();
        }
        if (mAudioFile!=null){
            mAudioFile.delete();
        }
        if(mFileFeng!=null){
            mFileFeng.delete();
        }
    }


}
