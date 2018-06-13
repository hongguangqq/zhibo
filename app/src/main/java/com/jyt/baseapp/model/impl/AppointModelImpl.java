package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.AppointModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/6/13 17:22
 */
public class AppointModelImpl implements AppointModel {
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
    public void getMyOrder(Callback callback) {
        String path = null;
        if (Const.getGender()==1){
            //男性
            path = Path.MaleAppointmentList;
        }else {
            //女性
            path = Path.FemaleAppointmentList;
        }
        OkHttpUtils.get()
                .url(path)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }


    @Override
    public void MakeAppointment(int userId, int length, Callback callback) {
        OkHttpUtils.post()
                .url(Path.InsertAppointment)
                .tag(mContext)
                .addHeader("token",Const.getUserToken())
                .addParams("userId",String.valueOf(userId))
                .addParams("length",String.valueOf(length))
                .build()
                .execute(callback);
    }

    @Override
    public void CancelOrder(int id, Callback callback) {
        OkHttpUtils.get()
                .url(Path.DeleteAppoint)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("subId",String.valueOf(id))
                .build()
                .execute(callback);
    }
}
