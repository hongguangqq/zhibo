package com.jyt.baseapp.bean;

import com.xiaomi.mipush.sdk.MiPushMessage;

/**
 * @author LinWei on 2018/7/20 21:39
 */
public class PushMessageBean {
    private long endTime;
    private MiPushMessage mMiPushMessage;

    public PushMessageBean(long endTime, MiPushMessage miPushMessage) {
        this.endTime = endTime;
        mMiPushMessage = miPushMessage;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public MiPushMessage getMiPushMessage() {
        return mMiPushMessage;
    }

    public void setMiPushMessage(MiPushMessage miPushMessage) {
        mMiPushMessage = miPushMessage;
    }
}
