package com.jyt.baseapp.bean;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2018/6/1 15:17
 */
public class DynamicBean implements Serializable {
    private int id;
    private UserBean userId;
    private String text;
    private String imgs;
    private String video;
    private String time;
    private String vedioImg;
    private List likeUsers;
    private boolean flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserBean getUserId() {
        return userId;
    }

    public void setUserId(UserBean userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List getLikeUsers() {
        return likeUsers;
    }

    public void setLikeUsers(List likeUsers) {
        this.likeUsers = likeUsers;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getVedioImg() {
        return vedioImg;
    }

    public void setVedioImg(String vedioImg) {
        this.vedioImg = vedioImg;
    }

    public List<String> getImgsPath(){
        List<String> pathlist = null;
        if (imgs!=null){
            try {
                pathlist = new ArrayList<>();
                JSONArray js = new JSONArray(imgs);
                for (int i = 0; i < js.length();i++) {
                    pathlist.add(js.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pathlist;
    }
}
