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
 * @author LinWei on 2018/7/18 10:52
 */
@MessageTag(
        value = "app:BarrageMsg",
        flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED
)
public class BarrageMessage extends MessageContent {
    private String name;
    private String danmu;
    private String img;

    public BarrageMessage(String name, String danmu, String img) {
        this.name = name;
        this.danmu = danmu;
        this.img = img;
    }



    public BarrageMessage(byte[] data){
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("name"))
                name = jsonObj.optString("name");

            if (jsonObj.has("danmu"))
                danmu = jsonObj.optString("danmu");

            if (jsonObj.has("img"))
                img = jsonObj.optString("img");

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

    public BarrageMessage(Parcel in){
        name = ParcelUtils.readFromParcel(in);
        danmu = ParcelUtils.readFromParcel(in);
        img = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setMentionedInfo((MentionedInfo)ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", name);
            jsonObj.put("danmu", danmu);
            jsonObj.put("img", img);
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
    public static final Creator<BarrageMessage> CREATOR = new Creator<BarrageMessage>() {

        @Override
        public BarrageMessage createFromParcel(Parcel source) {
            return new BarrageMessage(source);
        }

        @Override
        public BarrageMessage[] newArray(int size) {
            return new BarrageMessage[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ParcelUtils.writeToParcel(parcel,name);
        ParcelUtils.writeToParcel(parcel,danmu);
        ParcelUtils.writeToParcel(parcel,img);
        ParcelUtils.writeToParcel(parcel, this.getUserInfo());
        ParcelUtils.writeToParcel(parcel, this.getMentionedInfo());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDanmu() {
        return danmu;
    }

    public void setDanmu(String danmu) {
        this.danmu = danmu;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
