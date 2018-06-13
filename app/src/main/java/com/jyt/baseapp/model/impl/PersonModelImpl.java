package com.jyt.baseapp.model.impl;

import android.content.Context;
import android.util.Log;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.util.MD5Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


/**
 * @author LinWei on 2018/5/30 19:24
 */
public class PersonModelImpl implements PersonModel {
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
    public void getFansCount(Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetFansCount)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }

    @Override
    public void getFollowCount(Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetFollowCount)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }

    @Override
    public void getWalletAccount(int size, int page, Callback callback) {

    }

    @Override
    public void getMyUserData(Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetMyUserData)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }

    @Override
    public void getOtherData(int id, Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetOtherUserData)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("userId",String.valueOf(id))
                .build()
                .execute(callback);
    }

    @Override
    public void UpDateMyData(UserBean user, Callback callback) {

        user.setId(Integer.parseInt(Const.getUserID()));
        PostFormBuilder builder = OkHttpUtils.post()
                .url(Path.UpDateMyData)
                .tag(mContext)
                .addHeader("token", Const.getUserToken());
        Field fields[]=user.getClass().getDeclaredFields();//cHis 是实体类名称
        String[] name=new String[fields.length];
        Object[] value=new Object[fields.length];
        try {
            Field.setAccessible(fields, true);
            for (int i = 0; i < name.length; i++) {
                name[i] = fields[i].getName();
                System.out.println(name[i] + "-> ");
                value[i] = fields[i].get(user);//cHis 是实体类名称
                System.out.println(value[i]);
                if (fields[i].get(user)!=null){
                    builder.addParams(fields[i].getName(),  fields[i].get(user)+"");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.build().execute(callback);

    }

    @Override
    public void ToFollow(int userId, Callback callback) {
        OkHttpUtils.post()
                .addHeader("token",Const.getUserToken())
                .tag(mContext)
                .url(Path.ToFollow)
                .addParams("userId",String.valueOf(userId))
                .build()
                .execute(callback);
    }

    @Override
    public void CancelFollow(int userId, Callback callback) {
        OkHttpUtils.post()
                .addHeader("token",Const.getUserToken())
                .tag(mContext)
                .url(Path.CancelFollow)
                .addParams("userId",String.valueOf(userId))
                .build()
                .execute(callback);
    }

    @Override
    public void PullBlack(int userId, Callback callback) {
        OkHttpUtils.post()
                .addHeader("token",Const.getUserToken())
                .tag(mContext)
                .url(Path.PullBack)
                .addParams("userId",String.valueOf(userId))
                .build()
                .execute(callback);
    }

    @Override
    public void ReportUser(int userId, String reason, Callback callback) {
        OkHttpUtils.post()
                .addHeader("token",Const.getUserToken())
                .tag(mContext)
                .url(Path.ReportUser)
                .addParams("userId",String.valueOf(userId))
                .addParams("reason",reason)
                .build()
                .execute(callback);
    }

    @Override
    public void setPrice(float price, Callback callback) {
        OkHttpUtils.post()
                .addHeader("token",Const.getUserToken())
                .tag(mContext)
                .url(Path.SetPrice)
                .addParams("price",String.valueOf(price))
                .build()
                .execute(callback);
    }

    @Override
    public void GetBlackList(Callback callback) {
        OkHttpUtils.get()
                .addHeader("token",Const.getUserToken())
                .tag(mContext)
                .url(Path.BlackList)
                .build()
                .execute(callback);
    }

    @Override
    public void DeleteBlackList(int id, Callback callback) {
        OkHttpUtils.post()
                .tag(mContext)
                .url(Path.DeleteBlackList)
                .addHeader("token",Const.getUserToken())
                .addParams("userId",String.valueOf(id))
                .build()
                .execute(callback);
    }

    @Override
    public void GetFollowList(int page, int size, Callback callback) {
        OkHttpUtils.get()
                .tag(mContext)
                .url(Path.GetFollowList)
                .addHeader("token",Const.getUserToken())
                .addParams("page",String.valueOf(page))
                .addParams("size",String.valueOf(size))
                .build()
                .execute(callback);
    }

    @Override
    public void GetFansList(int page, int size, Callback callback) {
        OkHttpUtils.get()
                .tag(mContext)
                .url(Path.GetFansList)
                .addHeader("token",Const.getUserToken())
                .addParams("page",String.valueOf(page))
                .addParams("size",String.valueOf(size))
                .build()
                .execute(callback);
    }

    @Override
    public void FeedBack(String img, String content, Callback callback) {
        PostFormBuilder builder = OkHttpUtils.post();
        builder.tag(mContext)
                .url(Path.FeedBack);
        if (img!=null){
            builder.addParams("img",img);
        }
        builder.addParams("content",content);
        builder.build().execute(callback);

    }

    @Override
    public void GetRongID(Callback callback) {
        int nonce = (int) (Math.random()*100);
        long Timestamp = new Date().getTime();
        String Signature = null;
        try {
            Signature = MD5Util.sha1(Const.RongYunSecret+nonce+Timestamp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.e("@#","1--"+nonce);
        Log.e("@#","2--"+Timestamp);
        Log.e("@#","3--"+Signature);

        OkHttpUtils.post()
                .url(Path.RongYunPath)
                .tag(mContext)
                .addHeader("RC-App-Key",Const.RongYunKey)
                .addHeader("RC-Nonce",nonce+"")
                .addHeader("RC-Timestamp",Timestamp+"")
                .addHeader("RC-Signature",Signature)
                .addParams("userId",Const.getUserID())
                .addParams("name",Const.getUserNick())
                .addParams("portraitUri",Const.getUserHeadImg())
                .build()
                .execute(callback);
    }

}
