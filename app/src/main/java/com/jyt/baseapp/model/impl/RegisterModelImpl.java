package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.RegisterModel;
import com.jyt.baseapp.util.MD5Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/24 15:53
 */
public class RegisterModelImpl implements RegisterModel {


    /**
     * 注册操作
     * @param phone
     * @param pwd
     * @param iode
     * @param nike
     * @param birthday
     * @param city
     * @param sex
     * @param callback
     */
    @Override
    public void Register(String phone,String vode ,String pwd, String iode, String nike, String birthday, String city, int sex, Callback callback) {
        OkHttpUtils.post()
                .url(Path.TelRegister)
                .addParams("phone",phone)
                .addParams("code",vode)
                .addParams("pwd", MD5Util.encrypt(pwd))
                .addParams("inviteCode",iode)
                .addParams("nickname",nike)
                .addParams("birthday",birthday)
                .addParams("cityName",city)
                .addParams("gender",sex+"")
                .tag(mContext)
                .build()
                .execute(callback);
    }

    @Override
    public void RegisterByQW(String qw,boolean QOW, String tel, String vode, String nike, String birthday, String city, int sex, Callback callback) {
        String openQW = null;
        if (QOW){
            openQW="qqOpenId";
        }else {
            openQW="wxOpenId";
        }
        OkHttpUtils.post()
                .url(Path.QWRegister)
                .addParams("phone",tel)
                .addParams("code",vode)
                .addParams(openQW,qw)
                .addParams("nickname",nike)
                .addParams("birthday",birthday)
                .addParams("cityName",city)
                .addParams("gender",sex+"")
                .tag(mContext)
                .build()
                .execute(callback);
    }


    /**
     * 获取验证码
     * @param tel
     */
    @Override
    public void GetInviteCode(String tel,Callback callback) {
        OkHttpUtils.post()
                .url(Path.GetVerification)
                .tag(mContext)
                .addParams("phone",tel)
                .build()
                .execute(callback);
    }


    private Context mContext;
    @Override
    public void onStart(Context context) {
        mContext = context;
    }

    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(mContext);
    }
}
