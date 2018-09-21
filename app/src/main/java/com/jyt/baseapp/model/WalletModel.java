package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/31 11:36
 */
public interface WalletModel extends BaseModel {

    void getBalance(Callback callback);

    void getWalletAccount(int page, int size, Callback callback);

    void aliPay(String amount , Callback callback);

    /**
     * 提现
     * @param userId
     * @param money
     * @param bankName
     * @param bankCard
     * @param realName
     * @param phone
     * @param callback
     */
    void putForward(String userId,String money,String bankName,String bankCard,String realName,String phone,Callback callback);

}
