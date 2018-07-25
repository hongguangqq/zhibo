package com.jyt.baseapp.view.widget;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.push.common.ParcelUtils;

/**
 * @author LinWei on 2018/7/25 10:42
 */
@MessageTag(
        value = "app:NMRCCallMessage",
        flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED
)
public class NMRCCallMessage extends MessageContent {
    private String roomName;//房间名
    private String nickname;//昵称
    private String headImg;//头像
    private String uId;//用户ID
    private String wId;//网易云用户ID
    private String trId;//通话记录ID
    private String code;//标识符


    public NMRCCallMessage(String code,String roomName , String nickname , String headImg , String uId , String wId , String trId){
        this.code = code;
        this.roomName = roomName;
        this.nickname = nickname;
        this.headImg = headImg;
        this.uId = uId;
        this.wId = wId;
        this.trId = trId;
    }

    public NMRCCallMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("roomName"))
                roomName = jsonObj.optString("roomName");

            if (jsonObj.has("nickname"))
                nickname = jsonObj.optString("nickname");

            if (jsonObj.has("headImg"))
                headImg = jsonObj.optString("headImg");

            if (jsonObj.has("uId"))
                uId = jsonObj.optString("uId");

            if (jsonObj.has("wId"))
                wId = jsonObj.optString("wId");

            if (jsonObj.has("trId"))
                trId = jsonObj.optString("trId");

            if (jsonObj.has("code"))
                code = jsonObj.optString("code");

            if(this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            if(this.getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", this.getJsonMentionInfo());
            }

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    public NMRCCallMessage(Parcel in){
        roomName = ParcelUtils.readFromParcel(in);
        nickname = ParcelUtils.readFromParcel(in);
        headImg = ParcelUtils.readFromParcel(in);
        uId = ParcelUtils.readFromParcel(in);
        wId = ParcelUtils.readFromParcel(in);
        trId = ParcelUtils.readFromParcel(in);
        code = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setMentionedInfo((MentionedInfo)ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }


    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("roomName", roomName);
            jsonObj.put("nickname", nickname);
            jsonObj.put("headImg", headImg);
            jsonObj.put("uId", uId);
            jsonObj.put("wId", wId);
            jsonObj.put("trId", trId);
            jsonObj.put("code", code);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<NMRCCallMessage> CREATOR = new Creator<NMRCCallMessage>() {

        @Override
        public NMRCCallMessage createFromParcel(Parcel source) {
            return new NMRCCallMessage(source);
        }

        @Override
        public NMRCCallMessage[] newArray(int size) {
            return new NMRCCallMessage[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ParcelUtils.writeToParcel(parcel,roomName);
        ParcelUtils.writeToParcel(parcel,nickname);
        ParcelUtils.writeToParcel(parcel,headImg);
        ParcelUtils.writeToParcel(parcel,uId);
        ParcelUtils.writeToParcel(parcel,wId);
        ParcelUtils.writeToParcel(parcel,trId);
        ParcelUtils.writeToParcel(parcel,code);
        ParcelUtils.writeToParcel(parcel, this.getUserInfo());
        ParcelUtils.writeToParcel(parcel, this.getMentionedInfo());
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getwId() {
        return wId;
    }

    public void setwId(String wId) {
        this.wId = wId;
    }

    public String getTrId() {
        return trId;
    }

    public void setTrId(String trId) {
        this.trId = trId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
