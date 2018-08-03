package com.jyt.baseapp.model;

import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/6/13 17:21
 */
public interface AppointModel extends BaseModel {

    void getMyOrder(Callback callback);

    void MakeAppointment(int userId , int length , Callback callback);

    void CancelOrder(int id , Callback callback);

    void getInOut(int page,Callback callback);

    void getWhoLokeMe(Callback callback);
}
