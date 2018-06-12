package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/24 15:44
 */
public interface RegisterModel extends BaseModel {
    void Register(String phone ,String vode,String pwd , String iode,String nike,String birthday,String city,int sex,Callback callback);

    void RegisterByQW(String qw,boolean QOW, String tel, String vode,String nike, String birthday, String city, int sex, Callback callback);

    void GetInviteCode(String tel,Callback callback);
}
