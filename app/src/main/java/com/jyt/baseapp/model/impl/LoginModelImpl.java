package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.LoginModel;
import com.jyt.baseapp.util.MD5Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/25 15:09
 */
public class LoginModelImpl implements LoginModel {

    private Context mContext;

    @Override
    public void onStart(Context context) {
        mContext = context;
    }

    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(mContext);
    }

    @Override
    public void Login(String tel, String pwd, Callback callback) {
        OkHttpUtils.post()
                .url(Path.Login)
                .tag(mContext)
                .addParams("phone",tel)
                .addParams("pwd", MD5Util.encrypt(pwd))
                .addParams("loginType","3")
                .build()
                .writeTimeOut(1000*10)
                .execute(callback);
    }

    @Override
    public void LoginByQW(String QWNumber,boolean QOW ,Callback callback) {
        String openQW = null;
        String loginType =null;
        if (QOW){
            openQW="qqOpenId";
            loginType = "1";
        }else {
            openQW="wxOpenId";
            loginType = "2";
        }
        OkHttpUtils.post()
                .url(Path.Login)
                .tag(mContext)
                .addParams(openQW,QWNumber)
                .addParams("loginType",loginType)//1/QQ 2/WeChat
                .build()
                .readTimeOut(1000*5)
                .execute(callback);
    }

    @Override
    public void LogOff(Callback callback) {
        OkHttpUtils.post()
                .url(Path.LogOff)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }
}
