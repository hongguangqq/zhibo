package com.jyt.baseapp.service;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jyt.baseapp.App;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.CallBean;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.manager.LiveManager;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.FinishActivityManager;
import com.jyt.baseapp.view.activity.AudienceActivity;
import com.jyt.baseapp.view.activity.LivePlayActivity;
import com.jyt.baseapp.view.widget.MyInterruptLinearLayout;
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
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoQualityStrategy;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatLiveCompositingLayout;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * @author LinWei on 2018/6/25 10:39
 */
public class ScannerManager  {
    private static final String TAG = "@#";
    private static AVChatCameraCapturer mVideoCapturer;//美颜设置
    private static AVChatSurfaceViewRenderer mLocalRender;//大屏显示的video-远程
    private static AVChatSurfaceViewRenderer mRemoterRender;//小屏显示的video-本地
    public static String mMeetingName;//房间名
    public static long starComTime;//聊天开始时间
    public static long endComTome;//聊天结束时间
    public static boolean isEavesdrop;//是否为偷听者
    public static boolean isStartLive;//是否在连线
    public static boolean isBigScreen;//是否处于大屏状态
    private static boolean isOpenAudio;//是否开启音频
    private static boolean isOpenVideo;//是否开启视频
    public static boolean isAudienceJoin;//聊天对象是否已进入
    public static boolean isRingBack;//是否为回拨
    public static int mEavesdropNum;//偷听人数
    public static String uId = "";//用户ID
    public static String subId = "";//预约条目ID
    public static String trId ="";//通话记录的ID
    public static String comID = "";//聊天对象网易云ID
    public static int LastRequestNum;//一次直播中，只允许有3次查看对方余额的机会

    private static MyInterruptLinearLayout mLlRemoteLayout;


    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams wmParams;
    private static boolean mHasShown;
    private static LiveModel mLiveModel;

    public static void init(final Context context) {
        mhandle = new Handler();
        isOpenAudio = true;
        isOpenVideo = true;
        isAudienceJoin = false;
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(context);
        wmParams = new WindowManager.LayoutParams();
        //设置Render
        mLlRemoteLayout = new MyInterruptLinearLayout(context);
        mRemoterRender = new AVChatSurfaceViewRenderer(context);
        mLocalRender = new AVChatSurfaceViewRenderer(context);
        //false true主播可行  true false观众可行
        mLocalRender.setZOrderMediaOverlay(true);
        mRemoterRender.setZOrderMediaOverlay(false);
        ViewGroup parent1 = (ViewGroup) mLocalRender.getParent();
        if (parent1!=null){
            parent1.removeView(mLocalRender);
        }
        ViewGroup parent2 = (ViewGroup) mRemoterRender.getParent();
        if (parent2!=null){
            parent2.removeView(mRemoterRender);
        }
        mLlRemoteLayout.setBackgroundColor(Color.parseColor("#ff0000"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200,200);
        mLlRemoteLayout.setLayoutParams(params);
        //设置完毕
        mLlRemoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("@#","点击事件触发");
                if (!isBigScreen){
                    hide();
                    if (Const.getGender() == 1){
                        //男性
                        IntentHelper.OpenAudienceActivity(context);
                    }else {
                        //女性
                        IntentHelper.OpenLivePlayActivity(context);
                    }
                }
            }
        });
        startPreview(context);



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

    public static void setWindowType(final Context context){
        WindowManager windowManager = getWindowManager(context);
        if (Build.VERSION.SDK_INT >= 23) { /*android7.0不能用TYPE_TOAST*/

            String packname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                Log.e("@#","TYPE_PHONE");
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                Log.e("@#","TYPE_TOAST");
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;

            }
        } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
            wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        wmParams.format = PixelFormat.TRANSLUCENT;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //调整悬浮窗显示的停靠位置为右侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
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
        //设置悬浮窗口长宽数据
        wmParams.width = BaseUtil.dip2px(101);
        wmParams.height = BaseUtil.dip2px(151);
        wmParams.x = screenWidth- wmParams.width-BaseUtil.dip2px(30);
        wmParams.y = 20;


//        mLocalRender.setLayoutParams(wmParams);
//        mHasShown = true;
//        windowManager.addView(mLocalRender, wmParams);

    }

    /**
     * 主播创建房间并加入
     * @param context
     */
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
                        starComTime = System.currentTimeMillis();//主播进入房间，记录开始时间
                        if (isRingBack){
                            //主播回拨
                            mLiveModel.RingBack(ScannerManager.uId, 2, mMeetingName, new BeanCallback<BaseJson<CallBean>>() {
                                @Override
                                public void response(boolean success, BaseJson<CallBean> response, int id) {
                                    if (success && response.getCode()==200){
                                        trId = String.valueOf(response.getData().getId());
                                        LiveManager.LiveRingBack(mMeetingName,uId);
                                    }

                                }
                            });
                        }else {
                            //主播接听
                            mLiveModel.AnchorAnswer(trId, mMeetingName, Const.getWyAccount(), new BeanCallback() {
                                @Override
                                public void response(boolean success, Object response, int id) {
                                    LiveManager.liveAgreeCreatRoom(mMeetingName,uId);
                                }
                            });
                        }

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

    /**
     * 通话对象加入
     * @param context
     * @param roomId
     * @param comId
     */
    public static void joinRoom(Context context, String roomId, final String comId){
        AVChatManager.getInstance().joinRoom2(roomId, AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                Log.e(TAG , "join channel success"+"/comid="+comId);
                isStartLive = true;
                isAudienceJoin = true;
                isPause = false;
                AVChatManager.getInstance().setupLocalVideoRender(mLocalRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
                starComTime = System.currentTimeMillis();//观众进入房间，记录开始时间
                //每分钟连接服务器报告通话状况
                mhandle.postDelayed(timeRunable,60*1000);
            }

            @Override
            public void onFailed(int i) {
                Log.e(TAG , "join channel failed, code:" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG , "join channel exception, throwable:" + throwable.getMessage());
            }
        });
    }


    /**
     * 服务关闭时启动
     */
    public static void closeConnection(){
        mLiveModel.onDestroy();
        ViewGroup parent1 = (ViewGroup) mLocalRender.getParent();
        if (parent1!=null){
            parent1.removeView(mLocalRender);
        }
        if (isBigScreen){
            ViewGroup parent2 = (ViewGroup) mRemoterRender.getParent();
            if (parent2!=null){
                parent2.removeView(mRemoterRender);
            }
        }
        AVChatManager.getInstance().disableRtc();
        AVChatManager.getInstance().disableVideo();
        mLocalRender = null;
        mRemoterRender = null;
        isStartLive = false;
        isPause = true;
        mMeetingName = "";
        comID = "";
        LastRequestNum = 0;//重置零
        mEavesdropNum = 0;

    }



    public static AVChatSurfaceViewRenderer getmLocalRender(){
        return mLocalRender;
    }

    public static AVChatSurfaceViewRenderer getmRemoteRender(){
        return mRemoterRender;
    }

    public static void hide() {
        if (mHasShown)
            mWindowManager.removeViewImmediate(mLlRemoteLayout);
            mLlRemoteLayout.removeView(mRemoterRender);
            ViewGroup LocalParent = (ViewGroup) mLocalRender.getParent();
            if (LocalParent != null){
                LocalParent.removeView(mLocalRender);
            }
        mHasShown = false;
    }

    public static void show(Context context) {
        if (!mHasShown)
            mHasShown = true;
            isBigScreen = false;//切换为窗口模式
            setWindowType(context);
            ViewGroup parent = (ViewGroup) mRemoterRender.getParent();
            if (parent!=null){
                parent.removeView(mRemoterRender);

            }
//            ViewGroup parent2 = (ViewGroup) mLlRemoteLayout.getParent();
//            if (parent2!=null){
//                parent2.removeView(mLlRemoteLayout);
//
//            }
            mLlRemoteLayout.addView(mRemoterRender);
            mWindowManager.addView(mLlRemoteLayout , wmParams);
    }

    /**
     * 移除悬浮窗
     */
    public static void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isBigScreen){
                isAttach = mLocalRender.isAttachedToWindow();
            }
        }
        if (mHasShown && isAttach && mWindowManager != null)
            mWindowManager.removeView(mRemoterRender);
            mHasShown = false;
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
        if (AVChatManager.getInstance().isAudienceRole()){
            AVChatManager.getInstance().enableAudienceRole(false);
        }
        if (true){

        }
        //设置视频采集模块
        AVChatCameraCapturer videoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
        AVChatManager.getInstance().setupVideoCapturer(videoCapturer);
        if (true) {
            AVChatManager.getInstance().enableVideo();
            AVChatManager.getInstance().muteLocalVideo(true);//开启视频
            AVChatManager.getInstance().muteLocalAudio(true);//开启音频
            AVChatManager.getInstance().setupLocalVideoRender(mLocalRender, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            AVChatManager.getInstance().setVideoQualityStrategy(AVChatVideoQualityStrategy.PreferFrameRate);
        }
        //parameters
        AVChatParameters parameters = new AVChatParameters();
        parameters.setBoolean(AVChatParameters.KEY_SESSION_LIVE_MODE, true);
        parameters.setInteger(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        parameters.setString(AVChatParameters.KEY_VIDEO_ENCODER_MODE, AVChatMediaCodecMode.MEDIA_CODEC_SOFTWARE);
        parameters.setInteger(AVChatParameters.KEY_VIDEO_QUALITY, AVChatVideoQuality.QUALITY_720P);
        //如果用到美颜功能，建议这里设为15帧
        //        parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_RATE, AVChatVideoFrameRate.FRAME_RATE_15);
        //如果不用美颜功能，这里可以设为25帧
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FRAME_RATE, AVChatVideoFrameRate.FRAME_RATE_25);
        parameters.set(AVChatParameters.KEY_SESSION_LIVE_COMPOSITING_LAYOUT, new AVChatLiveCompositingLayout(AVChatLiveCompositingLayout.Mode.LAYOUT_FLOATING_RIGHT_VERTICAL));
        parameters.setInteger(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_16_9);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_ROTATE_IN_RENDING, false);
        int videoOrientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? AVChatVideoCaptureOrientation.ORIENTATION_PORTRAIT : AVChatVideoCaptureOrientation.ORIENTATION_LANDSCAPE_RIGHT;
        parameters.setInteger(AVChatParameters.KEY_VIDEO_CAPTURE_ORIENTATION, videoOrientation);
        parameters.setBoolean(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true);

        AVChatManager.getInstance().setParameters(parameters);
        if (true){
            AVChatManager.getInstance().startVideoPreview();
        }
    }

    public static void onUserJoin(String s){
        if (Const.getGender()==1){
            //男
            if (!TextUtils.isEmpty(ScannerManager.comID) && ScannerManager.comID.equals(s)){
                isAudienceJoin = true;

                Log.e("@#","观众：男用户进入房间，连接主播视屏");
                AVChatManager.getInstance().setupRemoteVideoRender(ScannerManager.comID, mRemoterRender,false,AVChatVideoScalingType.SCALE_ASPECT_BALANCED);

            }
        } else {
            //女
            if (!TextUtils.isEmpty(ScannerManager.comID) && ScannerManager.comID.equals(s)){
                isAudienceJoin = true;
                Log.e("@#","主播：男用户进入房间，连接用户视屏");
                AVChatManager.getInstance().setupRemoteVideoRender(s,mRemoterRender,false,AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            }
        }
        EventBus.getDefault().post(new EventBean(Const.Event_UserJoin));
    }

    public static void onUserLeave(String s, int i) {
        //当聊天对象退出时，同时退出房间
        if (!TextUtils.isEmpty(comID) && s.equals(comID)){
            isStartLive = false;
            BaseUtil.makeText("聊天对象已退出，请等待退出");
            releaseRtc(null,true,true);//退出操作
        }else {
            EventBus.getDefault().post(new EventBean(Const.Event_UserLeave));
        }
    }

    public static void muteLocalVideo(){
        if (isOpenVideo){
            isOpenVideo = false;
            AVChatManager.getInstance().muteLocalVideo(false);
            BaseUtil.makeText("视频采集已关闭");
        } else {
            isOpenVideo = true;
            AVChatManager.getInstance().muteLocalVideo(true);
            BaseUtil.makeText("视频采集已开启");
        }

    }

    public static void muteLocalAudio(){
        if (isOpenAudio){
            isOpenAudio = false;
            AVChatManager.getInstance().muteLocalAudio(false);
            BaseUtil.makeText("音频采集已关闭");
        }else {
            isOpenAudio = true;
            AVChatManager.getInstance().muteLocalAudio(true);
            BaseUtil.makeText("音频采集已开启");
        }
    }


    public static void releaseRtc(final Activity activity , boolean isReleaseRtc, boolean isLeaveRoom) {
        //        if (mVideoEffect != null) {
        //            mVideoEffectHandler.post(new Runnable() {
        //                @Override
        //                public void run() {
        //                    Log.e("@#", "releaseRtc unInit");
        //                    mVideoEffect.unInit();
        //                    mVideoEffect = null;
        //                }
        //            });
        //        }

        if (isReleaseRtc) {
            AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();
            AVChatManager.getInstance().disableRtc();

        }
        if (isLeaveRoom) {
            //挂断电话
//            mLiveModel.DoneHangUp(new BeanCallback() {
//                @Override
//                public void response(boolean success, Object response, int id) {
//
//                }
//            });
            //离开房间
            AVChatManager.getInstance().leaveRoom2(ScannerManager.mMeetingName, new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("@#", "leave channel success");
                    if (activity != null) {
//                        activity.finish();
                    }
                }

                @Override
                public void onFailed(int i) {
                    Log.e("@#", "leave channel failed, code:" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    Log.e("@#", "leave channel exception, throwable:" + throwable.getMessage());
                }
            });
            endComTome = System.currentTimeMillis();//记录退出时间
            if (Const.getGender()==1){
                //男
                if (FinishActivityManager.getManager().IsActivityExist(AudienceActivity.class)){
                    EventBus.getDefault().post(new EventBean(Const.Event_Audience));
                }
            }else {
                //女
                if (FinishActivityManager.getManager().IsActivityExist(LivePlayActivity.class)){
                    EventBus.getDefault().post(new EventBean(Const.Event_Live));
                }
            }
            if (!ScannerManager.isBigScreen){
                Log.e("@#","窗口模式下触发离开");
                hide();
                if (Const.getGender() == 1){
                    IntentHelper.OpenEndCallActivity( App.getContext(),false);
                }else {
                    IntentHelper.OpenEndCallActivity( App.getContext(),true);
                }
                //窗口模式触发离开需要关闭服务
                ScannerController.getInstance().stopMonkServer(App.getContext());
            }
        }
    }

    //计时器
    private static Handler mhandle;
    private static boolean isPause = false;//是否暂停
    private static Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            if (!isPause) {
                //联网报告
                mLiveModel.ReportProgressTime(ScannerManager.trId, new BeanCallback() {
                    @Override
                    public void response(boolean success, Object response, int id) {
                        //报告目前金额，当金额不足时，主动离开房间
                    }
                });
                //递归调用本runable对象，实现每隔60秒一次执行任务
                mhandle.postDelayed(this, 60*1000);

            }
        }
    };


}
