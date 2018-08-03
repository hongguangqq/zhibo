package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/28 10:59
 */
public interface TabModel extends BaseModel{

    void getBunner(Callback callback);

    void getAllList(Callback callback);

    void getThemeList(int page , int size , Callback callback);

    void editOnlineState(int state,Callback callback);

    void getListData(int code ,int page, int size, Callback callback);

    void getVistorNum(long time , Callback callback);

    void joinActivity(int id ,Callback callback);

    void getActivityUser(int id ,int page ,Callback callback);



}
