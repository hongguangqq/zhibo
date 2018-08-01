package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.TabModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/28 11:43
 */
public class TabModelImpl implements TabModel {

    private Context mContext;

    /**
     * 获取轮播图数据
     * @param callback
     */
    @Override
    public void getBunner(Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetBunner)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }

    /**
     * 获得主界面热门、关注、男女用户、推荐列表数据
     * @param callback
     */
    @Override
    public void getAllList(Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetAllList)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }

    /**
     * 获取主界面主题列表数据
     * @param page
     * @param size
     * @param callback
     */
    @Override
    public void getThemeList(int page , int size , Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetThemeList)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("page",page+"")
                .addParams("size",size+"")
                .build()
                .execute(callback);
    }

    /**
     * 修改在线状态
     * @param state
     * @param callback
     */
    @Override
    public void editOnlineState(int state, Callback callback) {
        OkHttpUtils.post()
                .url(Path.EditOnlineState)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("onlineState",state+"")
                .build()
                .execute(callback);
    }


    /**
     *获取list数据 关注 热门 用户 推荐
     * @param code
     * @param page
     * @param size
     * @param callback
     */
    @Override
    public void getListData(int code, int page, int size, Callback callback) {
        switch (code){
            case 1:
                OkHttpUtils.get()
                        .url(Path.GetFocus)
                        .tag(mContext)
                        .addHeader("token", Const.getUserToken())
                        .addParams("page",page+"")
                        .addParams("size",size+"")
                        .build()
                        .execute(callback);
                break;

            case 2:
                OkHttpUtils.get()
                        .url(Path.GetHot)
                        .tag(mContext)
                        .addHeader("token", Const.getUserToken())
                        .addParams("type","5")
                        .addParams("page",page+"")
                        .addParams("size",size+"")
                        .build()
                        .execute(callback);
                break;

            case 3:
                int gender=1;
                if (Const.getGender()==1){
                    gender=2;
                }
                OkHttpUtils.get()
                        .url(Path.SearchKey+"?gender="+gender)
                        .tag(mContext)
                        .addHeader("token", Const.getUserToken())
                        .build()
                        .execute(callback);
                break;
            case 4:
                OkHttpUtils.get()
                        .url(Path.GetRecommend)
                        .tag(mContext)
                        .addHeader("token", Const.getUserToken())
                        .addParams("page",page+"")
                        .addParams("size",size+"")
                        .build()
                        .execute(callback);
                break;
        }
    }

    /**
     * 获取是看过我的人数
     * @param time
     * @param callback
     */
    @Override
    public void getVistorNum(long time, Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetVistorNum)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("date",String.valueOf(time))
                .build()
                .execute(callback);

    }


    @Override
    public void joinActivity(int id, Callback callback) {
        OkHttpUtils.get()
                .url(Path.JoinActivity)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("id",String.valueOf(id))
                .build()
                .execute(callback);
    }


    @Override
    public void onStart(Context context) {
        mContext = context;
    }

    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(mContext);
    }
}
