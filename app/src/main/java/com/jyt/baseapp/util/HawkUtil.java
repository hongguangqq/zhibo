package com.jyt.baseapp.util;

import android.os.Message;

import com.jyt.baseapp.bean.PushMessageBean;
import com.orhanobut.hawk.Hawk;
import com.xiaomi.mipush.sdk.MiPushMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2017/11/3 15:10
 */
public class HawkUtil {
    private static final String KEY_PSD = "KEY_SEARCH_HISTORY";
    private static final String KEY_PUSH= "KEY_PUSH";
    private static final String KEY_COM= "KEY_COM";

    public static List<String> getHistory(){
        List<String> onj= Hawk.get(KEY_PSD);
        if (onj==null){
            onj = new ArrayList<>();
        }
        return onj;
    }
    public static void addHistory(String bean){
        List<String> onj=getHistory();
        onj.add(bean);
        Hawk.put(KEY_PSD,onj);
    }

    public static void clearHistory(){
        Hawk.deleteAll();
    }

    public static void updateHistory(List<String> list){
        clearHistory();
        Hawk.put(KEY_PSD,list);
    }

    public static void addPushMessage(MiPushMessage message){
        PushMessageBean pushBean = new PushMessageBean(System.currentTimeMillis(),message);
        List<PushMessageBean> onj=getPushList();
        onj.add(pushBean);
        Hawk.put(KEY_PUSH,onj);
    }

    public static List<PushMessageBean> getPushList(){
        List<PushMessageBean> onj= Hawk.get(KEY_PUSH);
        if (onj==null){
            onj = new ArrayList<>();
        }
        return onj;
    }

    public static void addComMessage(Message message){
        List<Message> list = Hawk.get(KEY_COM);
        list.add(message);
    }


}
