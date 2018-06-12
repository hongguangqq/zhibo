package com.jyt.baseapp.model.impl;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.DynamicModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * @author LinWei on 2018/5/31 17:39
 */
public class DynamicModelImpl implements DynamicModel {
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
    public void ReleaseDynamic(String txt, List<String> picture, String video , String feng , Callback callback) {

        try {
            PostFormBuilder builder = OkHttpUtils.post()
                    .url(Path.ReleaseDynamic)
                    .tag(mContext)
                    .addHeader("token", Const.getUserToken());
            if (!TextUtils.isEmpty(txt)){
                builder.addParams("text", txt);
            }
            if (picture!=null && picture.size()>0){
                builder.addParams("imgs", new JSONArray(new Gson().toJson(picture)).toString());
            }
            if (video!=null){
                builder.addParams("video",video);
            }
            if (feng!=null){
                builder.addParams("vedioImg",feng);
            }
            builder.build().execute(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getDynamic(int page, int size, Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetDynamic)
                .addHeader("token", Const.getUserToken())
                .addParams("page",page+"")
                .addParams("size",size+"")
                .build()
                .execute(callback);

    }

    @Override
    public void SubmitZang(int treid, Callback callback) {
        OkHttpUtils.post()
                .url(Path.LikeZang)
                .addHeader("token", Const.getUserToken())
                .addParams("treId",treid+"")
                .build()
                .execute(callback);
    }

    @Override
    public void CancelZang(int treid, Callback callback) {
        OkHttpUtils.post()
                .url(Path.DisZang)
                .addHeader("token", Const.getUserToken())
                .addParams("treId",treid+"")
                .build()
                .execute(callback);
    }
}
