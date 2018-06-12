package com.jyt.baseapp.bean;

/**
 * @author LinWei on 2018/6/6 15:52
 */
public class PropagationBean {
    private boolean isVideo;
    private String path;


    public PropagationBean(boolean isVideo , String path){
        this.isVideo = isVideo;
        this.path = path;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
