package com.jyt.baseapp.model;

import com.jyt.baseapp.bean.UserBean;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/30 19:17
 */
public interface PersonModel extends BaseModel{


    void getFansCount(Callback callback);

    void getFollowCount(Callback callback);

    void getWalletAccount(int size, int page, Callback callback);

    void getMyUserData(Callback callback);

    void getOtherData(int id , Callback callback);

    void UpDateMyData(UserBean user , Callback callback);

    void ToFollow(int userId , Callback callback);

    void CancelFollow(int userId , Callback callback);

    void PullBlack(int userId , Callback callback);

    void ReportUser(int userId , String reason , Callback callback);

    void setPrice(float price ,Callback callback);

    void GetBlackList(Callback callback);

    void DeleteBlackList(int id ,Callback callback);

    void GetFollowList(int page , int size , Callback callback);

    void GetFansList(int page , int size , Callback callback);

    void FeedBack(String img , String content ,Callback callback);

    void getFocusIdList(Callback callback);

    void getUserData(int id,Callback callback);






}
