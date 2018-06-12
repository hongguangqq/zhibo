package com.jyt.baseapp.bean;

import java.util.List;

/**
 * @author LinWei on 2018/5/28 19:13
 */
public class ListBean {
    private int type;
    private List<UserBean> user;



    public List<UserBean> getList(){
        return user;
    }

    public int getType(){
        return type;
    }

}
