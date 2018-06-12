package com.jyt.baseapp.util;

import android.app.Activity;

import com.jyt.baseapp.api.Const;
import com.netease.cloud.nos.android.core.CallRet;
import com.netease.cloud.nos.android.core.Callback;
import com.netease.cloud.nos.android.core.UploadTaskExecutor;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.core.WanNOSObject;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.utils.Util;

import org.json.JSONException;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author LinWei on 2018/6/4 11:08
 */
public class WyManager {

    private static WyManager wyManager;
    private static UploadTaskExecutor executor;
    private WyManager(){

    }

    public static WyManager getManager(){
        if (wyManager==null){
            wyManager = new WyManager();
        }
        return wyManager;
    }

    public static void UploadWy(Activity activity , String path, final OnWyUploadListener listener) {
        long expires = System.currentTimeMillis() / 1000 + 3600 * 24 * 30 * 12 * 10;
        String uploadToken = null;
        final File f = new File(path);
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
            }else if (f.getName().contains("m4a")){
                wanNOSObject.setContentType("video/mpeg4");
            }

            wanNOSObject.setUploadToken(uploadToken);
            String uploadContext = Util.getData(activity, f.getAbsolutePath());
            executor = WanAccelerator.putFileByHttp(activity, f, f.getAbsoluteFile(), uploadContext, wanNOSObject, new Callback() {
                @Override
                public void onUploadContextCreate(Object o, String s, String s1) {
                    listener.onUploadContextCreate(o,s,s1);
                }

                @Override
                public void onProcess(Object o, long l, long l1) {
                    listener.onProcess(o,l,l1);
                }

                @Override
                public void onSuccess(CallRet callRet) {
                    executor = null;
                    listener.onSuccess(callRet);
                }

                @Override
                public void onFailure(CallRet callRet) {
                    listener.onFailure(callRet);
                }

                @Override
                public void onCanceled(CallRet callRet) {
                    listener.onCanceled(callRet);
                }
            });


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


    public interface OnWyUploadListener{
        void onUploadContextCreate(Object o, String s, String s1);
        void onProcess(Object o, long l, long l1);
        void onSuccess(CallRet callRet);
        void onFailure(CallRet callRet);
        void onCanceled(CallRet callRet);
    }
}
