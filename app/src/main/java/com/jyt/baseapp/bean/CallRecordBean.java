package com.jyt.baseapp.bean;

/**
 * @author LinWei on 2018/7/20 13:55
 */
public class CallRecordBean {
    private int id;
    private UserBean fromId;
    private UserBean toId;
    private String beginTime;
    private String endTime;
    private int length;
    private int state;
    private int price;
    private int type;
    private boolean in;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserBean getFromId() {
        return fromId;
    }

    public void setFromId(UserBean fromId) {
        this.fromId = fromId;
    }

    public UserBean getToId() {
        return toId;
    }

    public void setToId(UserBean toId) {
        this.toId = toId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }
}
