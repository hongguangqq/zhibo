package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.helper.IntentRequestCode;
import com.jyt.baseapp.model.DynamicModel;
import com.jyt.baseapp.model.impl.DynamicModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.LoadingDialog;
import com.jyt.baseapp.view.widget.SlFlowLayout;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.netease.cloud.nos.android.core.AcceleratorConf;
import com.netease.cloud.nos.android.core.CallRet;
import com.netease.cloud.nos.android.core.Callback;
import com.netease.cloud.nos.android.core.UploadTaskExecutor;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.core.WanNOSObject;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.utils.Util;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import org.json.JSONException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ReleaseActivity extends BaseMCVActivity {

    @BindView(R.id.et_release_input)
    EditText mEtInput;
    @BindView(R.id.iv_release_add)
    ImageView mIvAdd;
    @BindView(R.id.iv_release_p)
    ImageView mIvP;
    @BindView(R.id.iv_release_x)
    ImageView mIvX;
    @BindView(R.id.iv_release_d)
    ImageView mIvD;
    @BindView(R.id.rl_release_videio)
    RelativeLayout mRlVideo;
    @BindView(R.id.iv_release_video)
    ImageView mIvVideo;
    @BindView(R.id.ll_release_add)
    LinearLayout mLlAdd;
    @BindView(R.id.flow_hdynamic_photo)
    SlFlowLayout mFlowPhoto;


    private boolean isShow;//是否展开添加选项
    private ObjectAnimator mAddAnimator1;//添加按钮的动画
    private ObjectAnimator mAddAnimator2;//添加按钮的动画
    private ObjectAnimator mAnimatorIn;//添加选项进入的动画
    private ObjectAnimator mAnimatorOut;//添加选项退出的动画
    private List<String> mImageList;
    private List<String> mImageNameList;
    private AlertDialog mAlertDialog;
    private ViewGroup.LayoutParams params;
    private OnImageClickiListener listener;
    private OnImageLongClickListener longlistener;
    private File tempFile;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private ImagePicker imagePicker;
    private UploadTaskExecutor executor;
    private File mFileVideo;
    private File mFileFeng;
    private int State_PV = 0;//1为图片发布，2为视频发布
    private String mVideoPath;
    private String mVideoFeng;
    int t = 0;
    private DynamicModel mReleaseModel;
    private LoadingDialog mLoadingDialog;
    private final int CODE_UPLOAD = 123;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123) {
                String inputText = (String) msg.obj;
                mReleaseModel.ReleaseDynamic(inputText, mImageNameList, mVideoPath,mVideoFeng, new BeanCallback<BaseJson>() {
                    @Override
                    public void response(boolean success, BaseJson response, int id) {
                        if (success) {
                            if (response.getCode() == 200) {

                                if (mLoadingDialog.isShowing()) {
                                    mLoadingDialog.dismiss();
                                    BaseUtil.makeText("發佈成功");
                                }
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }
                });

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_release;
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
        setvMainBackground(R.mipmap.bg_entrance);
        setTextTitle("动态");
        showFunctionText();
        setFunctionText("确定");
        mReleaseModel = new DynamicModelImpl();
        mReleaseModel.onStart(this);
        mImageList = new ArrayList();
        mLoadingDialog = new LoadingDialog(this);
        mImageNameList = new ArrayList<>();
        listener = new OnImageClickiListener();
        longlistener = new OnImageLongClickListener();
        params = new RecyclerView.LayoutParams(BaseUtil.dip2px(95), BaseUtil.dip2px(95));
        mAddAnimator1 = new ObjectAnimator().ofFloat(mIvAdd, "rotation", 0, 45);
        mAddAnimator2 = new ObjectAnimator().ofFloat(mIvAdd, "rotation", 45, 0);
        mAnimatorIn = new ObjectAnimator().ofFloat(mLlAdd, "alpha", 0, 1);
        mAnimatorOut = new ObjectAnimator().ofFloat(mLlAdd, "alpha", 1, 0);
        imagePicker = new ImagePicker();
        //配置网易云
        AcceleratorConf conf = new AcceleratorConf();
        WanAccelerator.setConf(conf);


    }

    private void initSetting() {

        mAddAnimator1.setDuration(300);
        mAddAnimator2.setDuration(300);
        mAnimatorIn.setDuration(500);
        mAnimatorOut.setDuration(500);
        mIvP.setClickable(false);
        mIvX.setClickable(false);
        mIvD.setClickable(false);
        // 设置标题
        imagePicker.setTitle("裁剪");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
        mFlowPhoto.setChildSpacing(10);
        mFlowPhoto.setRowSpacing(10);
        mAnimatorIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIvP.setClickable(true);
                mIvX.setClickable(true);
                mIvD.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mAnimatorOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIvP.setClickable(false);
                mIvX.setClickable(false);
                mIvD.setClickable(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        setOnNextListener(new OnNextListener() {
            @Override
            public void next() {
                final String inputText = mEtInput.getText().toString();
                if (TextUtils.isEmpty(inputText) && State_PV == 0) {
                    BaseUtil.makeText("請填寫發佈內容");
                    return;
                }
                mLoadingDialog.show();
                if (!TextUtils.isEmpty(inputText) && State_PV == 0) {
                    Message message = new Message();
                    message.obj = inputText;
                    message.what = CODE_UPLOAD;
                    mHandler.sendMessage(message);
                }

                //上传视频操作
                if (mFileVideo != null) {
                    mVideoPath = Const.WangYi + "test/" + Const.getUserID() + "/" + mFileVideo.getName();
                    UploadWy(mFileVideo.getAbsolutePath(), new Callback() {
                        @Override
                        public void onUploadContextCreate(Object o, String s, String s1) {

                        }

                        @Override
                        public void onProcess(Object o, long l, long l1) {

                        }

                        @Override
                        public void onSuccess(CallRet callRet) {

                            mVideoFeng = Const.WangYi + "test/" + Const.getUserID() + "/" + mFileFeng.getName();
                            UploadWy(mFileFeng.getAbsolutePath(), new Callback() {
                                @Override
                                public void onUploadContextCreate(Object o, String s, String s1) {

                                }

                                @Override
                                public void onProcess(Object o, long l, long l1) {

                                }

                                @Override
                                public void onSuccess(CallRet callRet) {
                                    Message message = new Message();
                                    message.obj = inputText;
                                    message.what = CODE_UPLOAD;
                                    mHandler.sendMessage(message);
                                    executor = null;
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

//                    long expires = System.currentTimeMillis() / 1000 + 3600 * 24 * 30 * 12 * 10;
//                    String uploadToken = null;
//                    try {
//                        uploadToken = Util.getToken("asset", "test/" + Const.getUserID() + "/" + mFileVideo.getName(), expires, Const.WyAccessKey, Const.WySecretKey);
//                        WanNOSObject wanNOSObject = new WanNOSObject();
//                        wanNOSObject.setNosBucketName("asset");
//                        wanNOSObject.setNosObjectName("test/" + Const.getUserID() + "/" + mFileVideo.getName());
//                        wanNOSObject.setContentType("video/mpeg4");    // 请根据实际情况设置正确的MIME-TYPE
//                        wanNOSObject.setUploadToken(uploadToken);
//                        String uploadContext = Util.getData(ReleaseActivity.this, mFileVideo.getAbsolutePath());
//                        executor = WanAccelerator.putFileByHttp(ReleaseActivity.this, mFileVideo, mFileVideo.getAbsolutePath(), uploadContext, wanNOSObject, new Callback() {
//                            @Override
//                            public void onUploadContextCreate(Object o, String s, String s1) {
//
//                            }
//
//                            @Override
//                            public void onProcess(Object o, long l, long l1) {
//
//                            }
//
//                            @Override
//                            public void onSuccess(CallRet ret) {
//                                Message message = new Message();
//                                message.obj = inputText;
//                                message.what = CODE_UPLOAD;
//                                mHandler.sendMessage(message);
//                                executor = null;
//
//
//                            }
//
//                            @Override
//                            public void onFailure(CallRet ret) {
//                                executor = null;
//                            }
//
//                            @Override
//                            public void onCanceled(CallRet callRet) {
//
//                            }
//                        });
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (InvalidParameterException e) {
//                        e.printStackTrace();
//                    }

                }
                //上传图片操作
                if (mImageList != null && mImageList.size() != 0) {
                    for (int i = 0; i < mImageList.size(); i++) {
                        int lastxie = mImageList.get(i).lastIndexOf("/");
                        mImageNameList.add(Const.WangYi + "test/" + Const.getUserID() + "/" + mImageList.get(i).substring(lastxie + 1));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < mImageList.size(); i++) {
                                UploadWy(mImageList.get(i), new Callback() {
                                    @Override
                                    public void onUploadContextCreate(Object o, String s, String s1) {

                                    }

                                    @Override
                                    public void onProcess(Object o, long l, long l1) {

                                    }

                                    @Override
                                    public void onSuccess(CallRet callRet) {
                                        executor = null;
                                        t++;
                                        //上传网易云完毕,之后将地址发给后台保存
                                        if (t == mImageList.size()) {
                                            Message message = new Message();
                                            message.obj = inputText;
                                            message.what = CODE_UPLOAD;
                                            mHandler.sendMessage(message);
                                        }
                                    }

                                    @Override
                                    public void onFailure(CallRet callRet) {

                                    }

                                    @Override
                                    public void onCanceled(CallRet callRet) {

                                    }
                                });
//                                long expires = System.currentTimeMillis() / 1000 + 3600 * 24 * 30 * 12 * 10;
//                                String uploadToken = null;
//                                final File f = new File(mImageList.get(i));
//                                try {
//                                    uploadToken = Util.getToken("asset", "test/" + Const.getUserID() + "/" + f.getName(), expires, Const.WyAccessKey, Const.WySecretKey);
//                                    WanNOSObject wanNOSObject = new WanNOSObject();
//                                    wanNOSObject.setNosBucketName("asset");
//                                    wanNOSObject.setNosObjectName("test/" + Const.getUserID() + "/" + f.getName());
//                                    if (f.getName().contains(".jpg")) {
//                                        wanNOSObject.setContentType("image/jpeg");
//                                    } else if (f.getName().contains(".png")) {
//                                        wanNOSObject.setContentType("image/png");
//                                    }
//
//                                    wanNOSObject.setUploadToken(uploadToken);
//                                    String uploadContext = Util.getData(ReleaseActivity.this, f.getAbsolutePath());
//                                    executor = WanAccelerator.putFileByHttp(ReleaseActivity.this, f, f.getAbsoluteFile(), uploadContext, wanNOSObject, new Callback() {
//                                        @Override
//                                        public void onUploadContextCreate(Object o, String s, String s1) {
//
//                                        }
//
//                                        @Override
//                                        public void onProcess(Object o, long l, long l1) {
//
//                                        }
//
//                                        @Override
//                                        public void onSuccess(CallRet ret) {
//
//                                            executor = null;
//                                            Util.setData(ReleaseActivity.this,
//                                                    f.getAbsolutePath(), "");
//                                            t++;
//                                            //上传网易云完毕,之后将地址发给后台保存
//                                            if (t == mImageList.size()) {
//                                                Message message = new Message();
//                                                message.obj = inputText;
//                                                message.what = CODE_UPLOAD;
//                                                mHandler.sendMessage(message);
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(CallRet ret) {
//                                            executor = null;
//                                        }
//
//                                        @Override
//                                        public void onCanceled(CallRet callRet) {
//
//                                        }
//                                    });
//
//                                } catch (NoSuchAlgorithmException e) {
//                                    e.printStackTrace();
//                                } catch (InvalidKeyException e) {
//                                    e.printStackTrace();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (InvalidParameterException e) {
//                                    e.printStackTrace();
//                                }


                            }
                        }
                    }).start();
                }
            }

        });


    }


    private void setPhotoListData() {
        if (mImageList != null) {
            mFlowPhoto.removeAllViews();
            for (int i = 0; i < mImageList.size(); i++) {
                ImageView iv = new ImageView(this);
                iv.setBackgroundColor(getResources().getColor(R.color.white));
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(this).load(mImageList.get(i)).into(iv);
                mFlowPhoto.addView(iv);
                iv.setTag(i);//设置位置index
                iv.setOnClickListener(listener);
                iv.setOnLongClickListener(longlistener);
            }
        }
    }


    /**
     * 选择图片或拍照
     */
    private void startChooser() {
        if (mImageList.size() >= 9) {
            return;
        }
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                ImageView iv = new ImageView(ReleaseActivity.this);
                iv.setBackgroundColor(getResources().getColor(R.color.white));
                iv.setLayoutParams(params);
                try {
                    tempFile = new File(new URI(imageUri.toString()));
                    mImageList.add(tempFile.getAbsolutePath());
                    setPhotoListData();
                    State_PV = 1;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(BaseUtil.getScannerWidth(), BaseUtil.getScannerHeight());
                // 宽高比
                //                        .setAspectRatio(1, 1);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
            }
        });
    }

    @OnClick(R.id.iv_release_add)
    public void Addshow() {
        if (!isShow) {
            mAddAnimator1.start();
            mAnimatorIn.start();
            isShow = true;
        } else {
            mAddAnimator2.start();
            mAnimatorOut.start();
            isShow = false;
        }
    }

    @OnClick(R.id.iv_release_x)
    public void Sel_x() {
        if (State_PV != 2) {
            if (mImageList.size() >= 9) {
                BaseUtil.makeText("发布的图片不可超过9张");
            } else {
                IntentHelper.openSelImageActivityForResult(this, 9, mImageList);
            }
        } else {
            BaseUtil.makeText("不允许视频与图片一同发布");
        }

    }

    @OnClick(R.id.iv_release_p)
    public void Sel_p() {
        if (State_PV != 2) {

            if (mImageList.size() >= 9) {
                BaseUtil.makeText("发布的图片不可超过9张");
            } else {
                startChooser();
            }
        } else {
            BaseUtil.makeText("不允许视频与图片一同发布");
        }
    }

    @OnClick(R.id.iv_release_d)
    public void Sel_d() {
        if (State_PV != 1) {
            IntentHelper.OpenRecordingActivity(this);
        } else {
            BaseUtil.makeText("不允许视频与图片一同发布");
        }


    }

    @OnClick(R.id.rl_release_videio)
    public void OpenVideoActivity() {
        IntentHelper.OpenVideoActivity(this, mFileVideo.getAbsolutePath());
    }

    @OnLongClick(R.id.rl_release_videio)
    public boolean OnLongClickVideo() {
        if (mFileVideo != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
            builder.setTitle("确定删除吗？");
            builder.setCancelable(false);
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFileVideo.delete();
                    mRlVideo.setVisibility(View.GONE);
                    mFileVideo = null;
                    State_PV = 0;
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCode.CODE_SEL_IMAGES && resultCode == RESULT_OK) {
            //相册选择
            Tuple result = IntentHelper.SelImageActivityGetResult(data);
            mImageList = (List) result.getItem1();
            setPhotoListData();
            State_PV = 1;
            return;
        } else if (requestCode == IntentRequestCode.CODE_RECORDING) {
            //视频拍摄
            Tuple result = IntentHelper.RecordingActivityGetPara(data);
            if (result == null) {
                return;
            }
            mRlVideo.setVisibility(View.VISIBLE);
            //视频地址
            if (mFileVideo != null) {
                mFileVideo.delete();
            }
            mFileVideo = new File((String) result.getItem1());
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(mFileVideo.getAbsolutePath());
            Bitmap bitmap = media.getFrameAtTime();
            //封面图片
            mFileFeng = BaseUtil.saveBitmap(ReleaseActivity.this,bitmap);
            mIvVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mIvVideo.setImageBitmap(bitmap);
            if (!mFileVideo.exists()) {
                BaseUtil.makeText("视频文件路径错误");
                return;
            }
            State_PV = 2;
            return;
        } else {
            //相机拍摄
            imagePicker.onActivityResult(this, requestCode, resultCode, data);
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    class OnImageClickiListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            IntentHelper.openBrowseImagesActivity(ReleaseActivity.this, mImageList, (Integer) v.getTag());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFileVideo != null) {
            mFileVideo.delete();
        }
        if (mFileFeng !=null){
            mFileFeng.delete();
        }
        mReleaseModel.onDestroy();
    }

    class OnImageLongClickListener implements View.OnLongClickListener {


        @Override
        public boolean onLongClick(final View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
            builder.setTitle("确定删除吗？");
            builder.setCancelable(false);
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int index = (int) v.getTag();
                    mImageList.remove(index);
                    setPhotoListData();
                    if (mImageList.size() == 0) {
                        State_PV = 0;
                    }
                }
            });

            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return false;
        }
    }


    private void UploadWy(String fileName, Callback callback) {
        long expires = System.currentTimeMillis() / 1000 + 3600 * 24 * 30 * 12 * 10;
        String uploadToken = null;
        final File f = new File(fileName);
        try {
            uploadToken = Util.getToken("asset", "test/" + Const.getUserID() + "/" + f.getName(), expires, Const.WyAccessKey, Const.WySecretKey);
            WanNOSObject wanNOSObject = new WanNOSObject();
            wanNOSObject.setNosBucketName("asset");
            wanNOSObject.setNosObjectName("test/" + Const.getUserID() + "/" + f.getName());
            if (f.getName().contains(".jpg")) {
                wanNOSObject.setContentType("image/jpeg");
            } else if (f.getName().contains(".png")) {
                wanNOSObject.setContentType("image/png");
            } else if (f.getName().contains("mp4")) {
                wanNOSObject.setContentType("video/mpeg4");
            }

            wanNOSObject.setUploadToken(uploadToken);
            String uploadContext = Util.getData(ReleaseActivity.this, f.getAbsolutePath());
            executor = WanAccelerator.putFileByHttp(ReleaseActivity.this, f, f.getAbsoluteFile(), uploadContext, wanNOSObject, callback);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
