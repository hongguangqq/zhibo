package com.jyt.baseapp.model.impl;

import android.content.Context;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.api.Path;
import com.jyt.baseapp.model.WalletModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/31 11:38
 */
public class WalletModelImpl implements WalletModel {
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
    public void getBalance(Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetBalance)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .build()
                .execute(callback);
    }

    @Override
    public void getWalletAccount(int page, int size, Callback callback) {
        OkHttpUtils.get()
                .url(Path.GetChargeData)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("page",page+"")
                .addParams("size",size+"")
                .addParams("type","4")
                .build()
                .execute(callback);
    }

    @Override
    public void aliPay(String amount, Callback callback) {
        OkHttpUtils.post()
                .url(Path.AliPay)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("amount",String.valueOf(amount))
                .build()
                .execute(callback);
    }

    @Override
    public void putForward(String userId, String money, String bankName, String bankCard, String realName, String phone, Callback callback) {
        OkHttpUtils.post()
                .url(Path.PutForward)
                .tag(mContext)
                .addHeader("token", Const.getUserToken())
                .addParams("userId",userId)
                .addParams("money",money)
                .addParams("bankName",bankName)
                .addParams("bankCard",bankCard)
                .addParams("realName",realName)
                .addParams("phone",phone)
                .build()
                .execute(callback);
    }
}
