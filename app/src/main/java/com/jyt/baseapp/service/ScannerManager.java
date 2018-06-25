package com.jyt.baseapp.service;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jyt.baseapp.util.BaseUtil;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatMediaCodecMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCaptureOrientation;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoFrameRate;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQuality;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatLiveCompositingLayout;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;

import java.util.UUID;

/**
 * @author LinWei on 2018/6/25 10:39
 */
public class ScannerManager  {
    private static AVChatCameraCapturer mVideoCapturer;
    private static AVChatSurfaceViewRenderer mLocalRender;//大屏显示的video
    private static AVChatSurfaceViewRenderer mBypassRender;//小屏显示的video
    public static String mMeetingName;//房间名
    public static boolean isStartLive;//是否在连线
    public static boolean isBigScreen = true;//是否处于大屏状态

    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams wmParams;
    private static boolean mHasShown;

    public static void init(Context context) {
        wmParams = new WindowManager.LayoutParams();
        //设置Render
        mLocalRender = new AVChatSurfaceViewRenderer(context);
        mBypassRender = new AVChatSurfaceViewRenderer(context);
        mLocalRender.setZOrderMediaOverlay(false);
        mBypassRender.setZOrderMediaOverlay(true);
        startPreview(context);
        //设置完毕
        setWindowType(context);
    }

    /**
     * 返回当前已创建的WindowManager。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public static void setWindowType(Context context){
        WindowManager windowManager = getWindowManager(context);
        if (Build.VERSION.SDK_INT >= 24) { /*android7.0不能用TYPE_TOAST*/
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
            String packname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
//        wmParams.x = screenWidth;
//        wmParams.y = screenHeight;
        wmParams.x = screenWidth-20;
        wmParams.y = 20;

        //设置悬浮窗口长宽数据
        wmParams.width = BaseUtil.dip2px(101);
        wmParams.height = BaseUtil.dip2px(151);
        mLocalRender.setLayoutParams(wmParams);
//        mHasShown = true;
//        windowManager.addView(mLocalRender, wmParams);
    }

    public static void createAndJoinRoom(Context context){
        mMeetingName = UUID.randomUUID().toString();
        AVChatManager.getInstance().createRoom(mMeetingName, null, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                Log.e("@#","room创建成功--"+ mMeetingName);
                AVChatManager.getInstance().joinRoom2(mMeetingName, AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
                    @Override
                    public void onSuccess(AVChatData avChatData) {
                        Log.e("@#", "Live join channel success");
                        isStartLive = true;

                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_EEXIST){
                    Log.e("@#","房间已存在");
                }else {
                    Log.e("@#","create room failed, code:" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                Log.e("@#", "create room onException, throwable:" + exception.getMessage());
            }
        });
    }


    public static void joinRoom(Context context,String roomId){

    }

    public static void clearRoom(Context context,String roomId){

    }



    public static AVChatSurfaceViewRenderer getmLocalRender(){
        return mLocalRender;
    }

    public static AVChatSurfaceViewRenderer getmBypassRender(){
        return mBypassRender;
    }

    public static void hide() {
        if (mHasShown)
            mWindowManager.removeViewImmediate(mLocalRender);
        mHasShown = false;
    }

    public static void show() {
        if (!mHasShown)
            Log.e("@#","show");
            ViewGroup parent = (ViewGroup) mLocalRender.getParent();
            if (parent!=null){
                parent.removeView(mLocalRender);
            }
            mWindowManager.addView(mLocalRender, wmParams);
        mHasShown = true;
    }

    /**
     * 移除悬浮窗
     */
    public static void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAttach = mLocalRender.isAttachedToWindow();
        }
        if (mHasShown && isAttach && mWindowManager != null)
            mWindowManager.removeView(mLocalRender);
    }


    /**
     * 主播直播前预览
     */
    private static void startPreview(Context context) {
        AVChatManager.getInstance().enableRtc();
        if (mVideoCapturer == null) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
            mVideoCapturer.setFlash(true);//设置闪光灯
        }
        if (true){
            AVChatManager.getInstance().enableVideo();
        }
        //设置视频采集模块
        AVChatCameraCapturer videoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
        AVChatManager.getInstance().setupVideoCapturer(videoCapturer);
        if (true) {
            AVChatManager.getInstance().setupLocalVideoRender(mLocalRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
        }
        //parameters
        AVChatParameters parameters = new AVChatParameters();
        parameters.setBoolean(AVChatParameters.KEY_SESSION_LIVE_MODE, true);
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        parameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
        parameters.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, AVChatVideoQuality.QUALITY_720P);
        //如果用到美颜功能，建议这里设为15帧
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_RATE, AVChatVideoFrameRate.FRAME_RATE_15);
        //如果不用美颜功能，这里可以设为25帧
        //parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_RATE, AVChatVideoFrameRate.FRAME_RATE_25);
        parameters.set(AVChatParameters.KEY_SESSION_LIVE_COMPOSITING_LAYOUT, new AVChatLiveCompositingLayout(AVChatLiveCompositingLayout.Mode.LAYOUT_FLOATING_RIGHT_VERTICAL));
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_16_9);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, true);
        int videoOrientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? AVChatVideoCaptureOrientation.ORIENTATION_PORTRAIT : AVChatVideoCaptureOrientation.ORIENTATION_LANDSCAPE_RIGHT;
        parameters.setInteger(AVChatParameters.KEY_VIDEO_CAPTURE_ORIENTATION, videoOrientation);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);
        AVChatManager.getInstance().setParameters(parameters);
        if (true){
            AVChatManager.getInstance().startVideoPreview();
        }
    }

}
