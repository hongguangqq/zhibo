package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/25 15:04
 */
public interface AuthModel extends BaseModel{

    void AuthInfo(int rid ,String name , String card ,String PhotoPath ,Callback callback);


}
