package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/6/22 15:25
 */
public interface LiveModel extends BaseModel{

    void MakeCall(int id ,int type ,Callback callback);
    void HangUp(int id ,int trid ,Callback callback);
}
