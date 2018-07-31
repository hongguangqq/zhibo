package com.jyt.baseapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.FinishActivityManager;
import com.jyt.baseapp.util.HawkUtil;
import com.jyt.baseapp.util.L;
import com.jyt.baseapp.view.activity.AnswerActivity;
import com.jyt.baseapp.view.activity.AnswerAudienceActivity;
import com.jyt.baseapp.view.activity.AudienceActivity;
import com.jyt.baseapp.view.activity.LaunchActivity;
import com.jyt.baseapp.view.activity.LivePlayActivity;
import com.jyt.baseapp.view.widget.BarrageMessage;
import com.jyt.baseapp.view.widget.NMRCCallMessage;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.push.RongPushClient;
import io.rong.push.common.RongException;
import okhttp3.OkHttpClient;

import static com.jyt.baseapp.api.Const.Event_NewArrive;

/**
 * Created by chenweiqi on 2017/10/30.
 */

public class App  extends MultiDexApplication {


    private static Context mcontext;
    private static Handler mhandler;
    private static int mainThreadid;
    private RefWatcher refWatcher;

    public boolean isDebug() {
        return isDebug;
    }
    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    private boolean isDebug = false;

    public static String weiXin_AppKey = "wx3b44ee4ad08755b6";
    public static String weiXin_AppSecret = "83dddf25ffe14ca901239b49ea241142";
    private Typeface typeface;

    private static final int MSG_SET_ALIAS = 1;
    static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_SET_ALIAS){
//                setJPushAlias();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mcontext=getApplicationContext();
        mhandler=new Handler();
        mainThreadid=android.os.Process.myTid();//主线程ID
        typeface = Typeface.createFromAsset(mcontext.getAssets(), "font/jyt.ttf");
        //判断目录是否存在
        File files = new File(Const.mMainFile);
        if (!files.exists()){
            //目录不存在，创建
            files.mkdirs();
        }
        initUtil();
//        MobSDK.init(this);
//        initMiPush();
        initRY();
        initNimLib();
//        refWatcher= setupLeakCanary();


    }

    public static  App getInstace() {
        return (App) mcontext;
    }


    private void initUtil(){
        Hawk.init(getApplicationContext()).setLogInterceptor(new LogInterceptor() {
            @Override
            public void onLog(String message) {
                if (isDebug()){
                    L.e(message);
                }
            }
        }).build();


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        LoggerInterceptor interceptor = new LoggerInterceptor("--HTTP--",true);
        builder.addInterceptor(interceptor ).hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).sslSocketFactory(createSSLSocketFactory());

        OkHttpUtils.initClient(builder.build());
    }

    private void initMiPush(){
        if(shouldInit()) {
            MiPushClient.registerPush(this, Const.MiAppID, Const.MiAppKey);
            //打开Log
            LoggerInterface newLogger = new LoggerInterface() {
                @Override
                public void setTag(String tag) {
                    // ignore
                }

                @Override
                public void log(String content, Throwable t) {
                    Log.d("@#", content, t);
                }

                @Override
                public void log(String content) {
                    Log.d("@#", content);
                }
            };
            Logger.setLogger(this, newLogger);
        }


    }

    private void initRY(){

        if (shouldInit()){
            RongPushClient.registerMiPush(this,Const.MiAppID,Const.MiAppKey);
        }
        RongIMClient.init(this);

        try {
            RongIMClient.registerMessageType(BarrageMessage.class);
            RongIMClient.registerMessageType(NMRCCallMessage.class);
            RongPushClient.checkManifest(this);
        } catch (RongException e) {
            Log.e("@#","rongyun:"+e.getMessage());
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }


        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(io.rong.imlib.model.Message message, int i) {
                Log.e("@#","onReceived");

                if (message.getContent() instanceof TextMessage
                        ||message.getContent() instanceof ImageMessage
                        ||message.getContent() instanceof VoiceMessage){
                    //好友消息加入List
                    HawkUtil.addComMessage(message);
                    //消息类型为默认类型时触发监听，Tab2刷新消息数量
                    EventBus.getDefault().post(new EventBean(Event_NewArrive));

                    //广播-同步更新文本通讯界面的消息
                    Intent intent = new Intent();
                    intent.setAction(Const.Reciver_Message);
                    intent.putExtra(Const.Rong_Message,message);
                    sendBroadcast(intent);
                }else if (message.getObjectName().equals("app:NMRCCallMessage")){
                    NMRCCallMessage msg = (NMRCCallMessage) message.getContent();
                    int code = Integer.valueOf(msg.getCode());
                    String name = msg.getNickname();
                    String hpic = msg.getHeadImg();
                    String uId = msg.getuId();
                    switch (code){
                        case 1:
                            //主播收到观众开播请求
                            BaseUtil.e("主播收到观众开播请求");
                            ScannerManager.isRingBack = false;
                            ScannerManager.comID = msg.getwId();
                            ScannerManager.trId = msg.getTrId();
                            ScannerManager.uId = uId;
                            IntentHelper.OpenAnswerActivity(mcontext,name,hpic,false);
                            break;
                        case 2:
                            BaseUtil.e("主播同意观众开播请求");
                            ScannerManager.isRingBack = false;
                            ScannerManager.comID = msg.getwId();
                            ScannerManager.trId = msg.getTrId();
                            ScannerManager.uId = uId;
                            ScannerManager.uId = uId;
                            ScannerManager.mMeetingName  = msg.getRoomName();
                            IntentHelper.OpenAudienceActivity(mcontext);
                            EventBus.getDefault().post(new EventBean(Const.Event_Launch));//拨打电话界面销毁
                            break;
                        case 3:
                            BaseUtil.e("观众未接听挂断");
                            if (FinishActivityManager.getManager().IsActivityExist(LivePlayActivity.class)){
                                EventBus.getDefault().post(new EventBean(Const.Event_HangUp));
                            }
                            if (FinishActivityManager.getManager().IsActivityExist(AnswerActivity.class)){
                                EventBus.getDefault().post(new EventBean(Const.Event_HangUp));
                            }
                            if (FinishActivityManager.getManager().IsActivityExist(AnswerAudienceActivity.class)){
                                EventBus.getDefault().post(new EventBean(Const.Event_HangUp));
                            }
                            break;
                        case 4:
                            BaseUtil.e("主播拒听");
                            if (FinishActivityManager.getManager().IsActivityExist(LaunchActivity.class)){
                                EventBus.getDefault().post(new EventBean(Const.Event_LiveHangUp));
                            }
                            break;
                        case 5:
                            BaseUtil.e("主播收到观众开播请求-音频");
                            ScannerManager.isRingBack = false;
                            ScannerManager.comID = msg.getwId();
                            ScannerManager.trId = msg.getTrId();
                            ScannerManager.uId = uId;
                            IntentHelper.OpenAnswerActivity(mcontext,name,hpic,true);
                            break;
                        case 6:
                            BaseUtil.e("主播同意观众开播请求-音频");
                            ScannerManager.comID = msg.getwId();
                            ScannerManager.trId = msg.getTrId();
                            ScannerManager.mMeetingName  = msg.getRoomName();
                            IntentHelper.OpenAudienceVoiceActivity(mcontext);
                            EventBus.getDefault().post(new EventBean(Const.Event_Launch));//拨打电话界面销毁
                            break;
                        case 7:
                            BaseUtil.e("观众收到主播回拨请求");
                            ScannerManager.isRingBack = true;
                            ScannerManager.comID = msg.getwId();
                            ScannerManager.trId = msg.getTrId();
                            ScannerManager.mMeetingName  = msg.getRoomName();
                            ScannerManager.uId = uId;
                            IntentHelper.OpenAnswerAudienceActivity(mcontext,name,hpic,uId,false);
                            break;

                    }
                    return true;
                }else if (message.getObjectName().equals("app:BarrageMsg")){
                    BarrageMessage bm = (BarrageMessage) message.getContent();
                    if (FinishActivityManager.getManager().IsActivityExist(LivePlayActivity.class)||
                        FinishActivityManager.getManager().IsActivityExist(AudienceActivity.class)){
                        EventBus.getDefault().post(bm);
                    }
                }
                return false;
            }
        });

    }

    private void initNimLib(){
        NIMClient.init(this, loginInfo(), options());
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
        }
    }

//
    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        App leakApplication = (App) context.getApplicationContext();
        return leakApplication.refWatcher;
    }

    //通过判断手机里的所有进程是否有这个App的进程
    //从而判断该App是否有打开
    private boolean shouldInit() {
        //通过ActivityManager我们可以获得系统里正在运行的activities
        //包括进程(Process)等、应用程序/包、服务(Service)、任务(Task)信息。
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();

        //获取本App的唯一标识
        int myPid = Process.myPid();
        //利用一个增强for循环取出手机里的所有进程
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            //通过比较进程的唯一标识和包名判断进程里是否存在该App
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }



    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
    public static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
//        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
//        config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用采用默认路径作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
//        String sdkPath = getAppCacheDir(context) + "/nim"; // 可以不设置，那么将采用默认路径
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
//        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
//        options.thumbnailSize = ${Screen.width} / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
//        options.userInfoProvider = new UserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String account) {
//                return null;
//            }
//
//            @Override
//            public int getDefaultIconResId() {
//                return R.drawable.avatar_def;
//            }
//
//            @Override
//            public Bitmap getTeamIcon(String tid) {
//                return null;
//            }
//
//            @Override
//            public Bitmap getAvatarForMessageNotifier(String account) {
//                return null;
//            }
//
//            @Override
//            public String getDisplayNameForMessageNotifier(String account, String sessionId,
//                                                           SessionTypeEnum sessionType) {
//                return null;
//            }
//        };
        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        LoginInfo loginInfo = null;
//        if (!TextUtils.isEmpty(Const.getWyAccount()) && TextUtils.isEmpty(Const.getWyToken())){
//            loginInfo = new LoginInfo(Const.getWyAccount(),Const.getWyToken());
//        }
        return loginInfo;
    }


    public static Context getContext() {
        return mcontext;
    }

    public static Handler getHandler() {
        return mhandler;
    }

    public static int getMainThreadid() {
        return mainThreadid;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    //region jpush
//    private void initJPush(){
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//        setJPushAlias();
//
//    }
//
//    public static void unInitJPush(){
//        JPushInterface.deleteAlias(App.getApplication(),0);
//    }
//
//    public static void setJPushAlias(){
//        if (!TextUtils.isEmpty(UserInfo.getToken())) {
//            JPushInterface.setAlias(app, UserInfo.getToken(), new TagAliasCallback() {
//                @Override
//                public void gotResult(int code, String s, Set<String> set) {
//                    String TAG = "JPush";
//                    String logs ;
//                    switch (code) {
//                        case 0:
//                            logs = "Set tag and alias success";
//                            Log.i(TAG, logs);
//                            // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//                            break;
//                        case 6002:
//                            logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                            Log.i(TAG, logs);
//                            handler.sendMessageDelayed(handler.obtainMessage(MSG_SET_ALIAS), 1000 * 60);
//
//                            break;
//                        default:
//                            logs = "Failed with errorCode = " + code;
//                            Log.e(TAG, logs);
//                    }
//                }
//            });
//        }
//    }
    //endregion
}
