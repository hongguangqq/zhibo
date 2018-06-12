package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/31 11:36
 */
public interface WalletModel extends BaseModel {

    void getBalance(Callback callback);

    void getWalletAccount(int page, int size, Callback callback);
}
