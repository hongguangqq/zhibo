package com.jyt.baseapp.model.impl;

import android.content.Context;
import android.text.TextUtils;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.bean.SearchConditionBean;
import com.jyt.baseapp.model.SearchModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/29 16:22
 */
public class SearchModelImpl implements SearchModel {
    private Context mContext;


    @Override
    public void SearchByKey(SearchConditionBean condition, Callback callback) {
        StringBuilder content = new StringBuilder();
        if (!TextUtils.isEmpty(condition.getNickname())){
            content.append("nickname="+condition.getNickname()+"&");
        }
        if (!TextUtils.isEmpty(condition.getSex())){
            content.append("gender="+condition.getSex()+"&");
        }
        if (!TextUtils.isEmpty(condition.getAge())){
            content.append("age="+condition.getAge()+"&");
        }
        if (!TextUtils.isEmpty(condition.getCity())){
            content.append("cityName="+condition.getCity()+"&");
        }
        if (!TextUtils.isEmpty(condition.getMoney())){
            content.append("price="+condition.getMoney()+"&");
        }
        if (!TextUtils.isEmpty(condition.getType())){
            content.append("type="+condition.getType()+"&");
        }
        if (!TextUtils.isEmpty(condition.getTime())){
            content.append("tiemLength="+condition.getTime()+"&");
        }
        content.append("page="+condition.getPage()+"&size="+condition.getSize());

        OkHttpUtils.get()
                .url(Path.SearchKey+"?"+content.toString())
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
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
