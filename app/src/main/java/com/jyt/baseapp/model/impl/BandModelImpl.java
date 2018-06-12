package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.BandModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/30 16:22
 */
public class BandModelImpl implements BandModel {

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
    public void getBangList(int type, int page, int size, Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetHot)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("type",type+"")
                .addParams("page",page+"")
                .addParams("size",size+"")
                .build()
                .execute(callback);
    }


    @Override
    public void getGiftData(int code, int page, int size, Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetHot)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("type",code+"")
                .addParams("page",page+"")
                .addParams("size",size+"")
                .build()
                .execute(callback);
    }

}
