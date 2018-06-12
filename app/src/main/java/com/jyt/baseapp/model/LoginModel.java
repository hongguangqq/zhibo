package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/25 15:04
 */
public interface LoginModel extends BaseModel{

    /**
     * 电话号码登录
     * @param tel
     * @param pwd
     * @param callback
     */
    void Login(String tel,String pwd,Callback callback);

    /**
     * 第三方登录
     * @param QWNumber
     */
    void LoginByQW(String QWNumber,boolean QOW,Callback callback);

    /**
     * 退出登录
     * @param callback
     */
    void LogOff(Callback callback);
}
