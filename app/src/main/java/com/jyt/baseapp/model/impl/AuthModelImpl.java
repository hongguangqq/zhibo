package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.AuthModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/25 15:09
 */
public class AuthModelImpl implements AuthModel {

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
    public void AuthInfo(int rid ,String name, String card, String PhotoPath, Callback callback) {
        OkHttpUtils.post()
                .url(Path.AuthInfoData)
                .addParams("userId",rid+"")
                .addParams("name",name)
                .addParams("idCard",card)
                .addParams("img",PhotoPath)
                .build()
                .execute(callback);
    }
}
