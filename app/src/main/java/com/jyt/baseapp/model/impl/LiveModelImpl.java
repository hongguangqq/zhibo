package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.service.ScannerManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import static com.jyt.baseapp.service.ScannerManager.trId;

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
     * @param id 用户ID
     * @param type 通话类型 1偷听 2视频 3语音
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

    /**
     * 偷听直播
     * @param callback
     */
    @Override
    public void EavesdropLive(Callback callback) {
        OkHttpUtils.get()
                .url(Path.EavesdropLive)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .build()
                .execute(callback);
    }

    @Override
    public void DoneHangUp(Callback callback) {
        OkHttpUtils.post()
                .url(Path.DongHangUp)
                .tag(mContext)
                .addParams("trId", trId)
                .addHeader("token",Const.getUserToken())
                .build()
                .execute(callback);
    }


    /**
     * 获取偷听人数
     * @param id
     * @param callback
     */
    @Override
    public void getEavesdropNum(int id, Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetEavesdropNum)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .addParams("userId",String.valueOf(id))
                .build()
                .execute(callback);
    }

    /**
     * 获取直播结束后的金额
     * @param isLive
     * @param callback
     */
    @Override
    public void getComFinishMoney(boolean isLive, Callback callback) {
        String code = isLive? "1":"0";
        OkHttpUtils.get()
                .url(Path.GetComMoney)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .addParams("isAnchor",code)
                .addParams("trId", trId)
                .build()
                .execute(callback);
    }

    /**
     * 每隔一分钟报告进度
     * @param trId
     * @param callback
     */
    @Override
    public void ReportProgressTime(String trId, Callback callback) {
        OkHttpUtils.get()
                .url(Path.ReportProgressTime)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .addParams("trId",trId)
                .build()
                .execute(callback);
    }

    /**
     * 获取弹幕列表
     * @param callback
     */
    @Override
    public void GetBarrageList(Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetBarrageList)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .build()
                .execute(callback);
    }

    /**
     * 发送弹幕
     * @param txt
     * @param callback
     */
    @Override
    public void SendBarrage(String txt, Callback callback) {
        OkHttpUtils.post()
                .url(Path.SendBarrage)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .addParams("trId", ScannerManager.trId)
                .addParams("danmu",txt)
                .build()
                .execute(callback);
    }


}
