package com.jyt.baseapp.bean;

/**
 * Created by chenweiqi on 2017/10/30.
 */

public class BaseJson <T>{
    private T data;
    private int code;
    private boolean ret;
    private boolean token;
    private String forUser;
    private String forWorker;
    private String message;

    public BaseJson() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public boolean isToken() {
        return token;
    }

    public void setToken(boolean token) {
        this.token = token;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public String getForWorker() {
        return forWorker;
    }

    public void setForWorker(String forWorker) {
        this.forWorker = forWorker;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }


    @Override
    public String toString() {
        return "BaseJson{" +
                "data=" + data +
                ", code=" + code +
                ", ret=" + ret +
                ", token=" + token +
                ", forUser='" + forUser + '\'' +
                ", forWorker='" + forWorker + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
