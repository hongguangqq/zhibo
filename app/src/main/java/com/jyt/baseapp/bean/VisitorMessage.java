package com.jyt.baseapp.bean;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.push.common.ParcelUtils;

/**
 * @author Linwei
 * @version $Rev$
 */
@MessageTag(
        value = "app:VisitorMessage",
        flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED
)
public class VisitorMessage extends MessageContent{
    private String fromUser;
    private String fromId;

    public VisitorMessage(String fromUser,String fromId){
        this.fromUser = fromUser;
        this.fromId = fromId;
    }

    public VisitorMessage(byte[] data){
        String jsonStr = null;
        try {
            jsonStr = new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("fromUser"))
                fromUser = jsonObj.optString("fromUser");

            if (jsonObj.has("fromId"))
                fromId = jsonObj.optString("fromId");

            if(this.getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", this.getJsonMentionInfo());
            }

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("fromUser", fromUser);
            jsonObj.put("fromId", fromId);

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
    public static final Creator<VisitorMessage> CREATOR = new Creator<VisitorMessage>() {

        @Override
        public VisitorMessage createFromParcel(Parcel source) {
            return new VisitorMessage(source);
        }

        @Override
        public VisitorMessage[] newArray(int size) {
            return new VisitorMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ParcelUtils.writeToParcel(parcel,fromUser);
        ParcelUtils.writeToParcel(parcel,fromId);
    }

    public VisitorMessage(Parcel in){
        fromUser = ParcelUtils.readFromParcel(in);
        fromId = ParcelUtils.readFromParcel(in);
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}
