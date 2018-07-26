package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/6/22 15:25
 */
public interface LiveModel extends BaseModel{

    void MakeCall(int id ,int type ,Callback callback);

    void HangUp(int id ,int trId ,Callback callback);

    void AnchorAnswer(String trId , String roomName , String accId , Callback callback);

    void RandomDialing(Callback callback);

    void EavesdropLive(Callback callback);

    void DoneHangUp(Callback callback);

    void getEavesdropNum(int id ,Callback callback);

    void getComFinishMoney(boolean isLive , Callback callback);

    void ReportProgressTime(String trId , Callback callback);

    void GetBarrageList(Callback callback);

    void SendBarrageGift(String txt , Callback callback);

    void GetUserBlance(Callback callback);

    void GetMeBlance(Callback callback);

    void GetBarrageGift(Callback callback);

    void RingBack(String userId , int type , String roomName , Callback callback);



}
