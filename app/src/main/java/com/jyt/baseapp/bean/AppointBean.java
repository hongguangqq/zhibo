package com.jyt.baseapp.bean;

/**
 * @author LinWei on 2018/6/13 16:49
 */
public class AppointBean {
    private int id;
    private UserBean fromId;
    private UserBean toId;
    private int length;
    private String createTime;
    private int state;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
