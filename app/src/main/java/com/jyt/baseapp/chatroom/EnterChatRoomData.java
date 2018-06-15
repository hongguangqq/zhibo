package com.jyt.baseapp.chatroom;

import android.text.TextUtils;

import java.util.Map;

/**
 * @author LinWei on 2018/6/15 15:19
 */
public class EnterChatRoomData {
    private String roomId;
    private boolean independentMode;
    private String account;
    private String token;
    private ChatRoomIndependentCallback independentModeCallback;
    private String nick;
    private String avatar;
    private Map<String, Object> extension;
    private Map<String, Object> notifyExtension;

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String var1) {
        this.roomId = var1;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String var1) {
        this.nick = var1;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String var1) {
        this.avatar = var1;
    }

    public Map<String, Object> getExtension() {
        return this.extension;
    }

    public void setExtension(Map<String, Object> var1) {
        this.extension = var1;
    }

    public Map<String, Object> getNotifyExtension() {
        return this.notifyExtension;
    }

    public void setNotifyExtension(Map<String, Object> var1) {
        this.notifyExtension = var1;
    }

    public boolean isIndependentMode() {
        return this.independentMode;
    }

    public String getAccount() {
        return this.account;
    }

    public String getToken() {
        return this.token;
    }

    public ChatRoomIndependentCallback getIndependentModeCallback() {
        return this.independentModeCallback;
    }

    public void setIndependentMode(ChatRoomIndependentCallback var1, String var2, String var3) {
        this.independentMode = true;
        this.independentModeCallback = var1;
        this.account = var2;
        this.token = var3;
    }

    public EnterChatRoomData(String var1) {
        this.roomId = var1;
    }

    public boolean isValid() {
        if(TextUtils.isEmpty(this.roomId)) {
            return false;
        } else {
            try {
                Long.parseLong(this.roomId);
                return true;
            } catch (NumberFormatException var1) {
                return false;
            }
        }
    }
}
