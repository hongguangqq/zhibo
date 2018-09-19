package com.jyt.baseapp.bean;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2018/5/25 16:03
 */
public class UserBean implements Serializable{

    private int id;
    private String nickname;
    private String phone;
    private String inviteCode;
    private String birthday;
    private int gender;
    private String cityName;
    private int attention;
    private int fans;
    private String token;
    private int onlineState;//线上状态 1在线 2离线
    private int anchorState;//是否通过验证
    private String headImg;
    private String imgs;
    private float price;
    private String mark;//备注
    private String introduction;//签名
    private String video;//视频
    private String voice;//声音
    private String vedioImg;
    private String profession;//职业
    private String miPush;
    private List<String> imgsList;
    private int age;
    private String easyId;//网易的AccessID
    private String easyToken;
    private String roomName;
    private long endTime;
    private int activityId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public int getOnlineState(){
        return onlineState;
    }

    public void setOnlineState(int onlineState){
        this.onlineState = onlineState;
    }

    public int getAnchorState(){
        return anchorState;
    }

    public void setAnchorState(int anchorState){
        this.anchorState = anchorState;
    }

    public void setHeadImg(String headImg){
        this.headImg = headImg;
    }

    public String getHeadImg(){
        return headImg;
    }

    public void setImgs(String imgs){
        this.imgs = imgs;
    }

    public String getImgs(){
        return imgs;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMiPush() {
        return miPush;
    }

    public void setMiPush(String miPush) {
        this.miPush = miPush;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getVedioImg() {
        return vedioImg;
    }

    public void setVedioImg(String vedioImg) {
        this.vedioImg = vedioImg;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEasyId() {
        return easyId;
    }

    public void setEasyId(String easyId) {
        this.easyId = easyId;
    }

    public String getEasyToken() {
        return easyToken;
    }

    public void setEasyToken(String easyToken) {
        this.easyToken = easyToken;
    }

    public List<String> getImgsArray(){

        if (imgs==null){
            return new ArrayList<>();
        }
        imgsList = new ArrayList<>();
        try {
            JSONArray js = new JSONArray(imgs);
            for (int i = 0; i < js.length(); i++) {
                imgsList.add((String) js.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imgsList;
    }

    public List<String> getImgsList(){
        if (imgsList==null){
            imgsList = new ArrayList<>();
        }
        return imgsList;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender=" + gender +
                ", cityName='" + cityName + '\'' +
                ", attention=" + attention +
                ", fans=" + fans +
                ", token='" + token + '\'' +
                ", onlineState=" + onlineState +
                ", anchorState=" + anchorState +
                ", headImg='" + headImg + '\'' +
                ", imgs='" + imgs + '\'' +
                ", price=" + price +
                ", mark='" + mark + '\'' +
                ", video='" + video + '\'' +
                ", voice='" + voice + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }
}
