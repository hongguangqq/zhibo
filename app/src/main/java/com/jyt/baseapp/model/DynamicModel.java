package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

/**
 * @author LinWei on 2018/5/31 17:36
 */
public interface DynamicModel extends BaseModel {

    void ReleaseDynamic(String txt , List<String> picture , String video , String feng ,Callback callback);

    void getDynamic(int  page , int size , Callback callback);

    void SubmitZang(int treId,Callback callback);

    void CancelZang(int treid,Callback callback);

}
