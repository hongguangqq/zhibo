package com.jyt.baseapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.rong.imlib.model.UserInfo;

/**
 * @author LinWei on 2018/7/26 11:34
 */
public class FriendNewsBean implements Parcelable {
    private int id;
    private String headImg;
    private String nickname;
    private long time;
    private String content;

    public FriendNewsBean(int id,String headImg,String nickname ,long time){
        this.id = id;
        this.headImg = headImg;
        this.nickname = nickname;
        this.time = time;
    }

    public FriendNewsBean(){

    }

    protected FriendNewsBean(Parcel in){
        id = in.readInt();
        headImg = in.readString();
        nickname = in.readString();
        time = in.readLong();
        content = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(headImg);
        parcel.writeString(nickname);
        parcel.writeLong(time);
        parcel.writeString(content);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserInfo(UserInfo userInfo){
        this.time = System.currentTimeMillis();
        this.id = Integer.valueOf(userInfo.getUserId());
        this.nickname = userInfo.getName();
        this.headImg = userInfo.getPortraitUri().toString();
    }
}
