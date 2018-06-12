package com.jyt.baseapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author LinWei on 2018/5/29 17:10
 */
public class SearchBean implements Serializable {
    private int number;
    private int size;
    private List<UserBean> content;

    public SearchBean(){}

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public List<UserBean> getContent() {
        return content;
    }


    public void setNumber(int number) {
        this.number = number;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setContent(List<UserBean> content) {
        this.content = content;
    }
}
