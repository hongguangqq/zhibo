package com.jyt.baseapp.bean;

import java.io.Serializable;

/**
 * @author LinWei on 2018/5/30 10:18
 */
public class SearchConditionBean implements Serializable{
    private String nickname;
    private String sex;
    private String age;
    private String city;
    private String money;
    private String type;
    private String time;
    private int page;
    private int size;

    public SearchConditionBean(String nickname){
        this.nickname = nickname;
        this.page = 0;
        this.size=10;
    }

    public SearchConditionBean(String sex, String age, String city, String money, String type, String time) {
        this.sex = sex;
        this.age = age;
        this.city = city;
        this.money = money;
        this.type = type;
        this.time = time;
        this.page = 0;
        this.size=10;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
