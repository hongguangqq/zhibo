package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/30 16:21
 */
public interface BandModel extends BaseModel{

    void getBangList(int type, int page, int size, Callback callback);

    void getGiftData(int code, int page, int size, Callback callback);

    void getGiftList(Callback callback);
}
