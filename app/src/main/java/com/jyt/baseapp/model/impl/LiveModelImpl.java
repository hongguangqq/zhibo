package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.LiveModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/6/22 16:02
 */
public class LiveModelImpl implements LiveModel {
    private Context mContext;
    @Override
    public void onStart(Context context) {
        mContext = context;
    }

    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(mContext);
    }

    /**
     * 用户拨打视频/音频给主播
     * @param id
     * @param type
     * @param callback
     */
    @Override
    public void MakeCall(int id, int type, Callback callback) {
        OkHttpUtils.post()
                .url(Path.MakeCall)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("userId",String.valueOf(id))
                .addParams("type",String.valueOf(type))
                .build()
                .execute(callback);
    }

    /**
     * 用户进入房间，开始计费
     * @param id
     * @param trId
     * @param callback
     */
    @Override
    public void HangUp(int id, int trId, Callback callback) {
        OkHttpUtils.post()
                .url(Path.HangUp)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("userId",String.valueOf(id))
                .addParams("trId",String.valueOf(trId))
                .build()
                .execute(callback);
    }

    /**
     * 主播创建房间
     * @param trId
     * @param roomName
     * @param accId
     * @param callback
     */
    @Override
    public void AnchorAnswer(String trId, String roomName, String accId, Callback callback) {
        OkHttpUtils.post()
                .url(Path.AnchorAnswer)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("trId",String.valueOf(trId))
                .addParams("roomName",roomName)
                .addParams("accId",accId)
                .build()
                .execute(callback);
    }

    /**
     * 随机拨号
     * @param callback
     */
    @Override
    public void RandomDialing(Callback callback) {
        OkHttpUtils.get()
                .url(Path.RandomRing)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .build()
                .execute(callback);
    }
}
