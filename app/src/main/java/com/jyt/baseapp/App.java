package com.jyt.baseapp;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.util.L;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

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

import io.rong.imlib.RongIMClient;
import io.rong.push.RongPushClient;
import okhttp3.OkHttpClient;

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
        initMiPush();
        initRY();
        initNimLib();
        refWatcher= setupLeakCanary();


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
        RongPushClient.registerMiPush(this,Const.MiAppID,Const.MiAppKey);
        RongIMClient.init(this);
        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(io.rong.imlib.model.Message message, int i) {
                Log.e("@#","onReceived");
                return false;
            }
        });
    }

    private void initNimLib(){

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
