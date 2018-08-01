package com.jyt.baseapp.util;


import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.bean.FriendNewsBean;
import com.jyt.baseapp.bean.PushMessageBean;
import com.jyt.baseapp.bean.UserBean;
import com.orhanobut.hawk.Hawk;
import com.xiaomi.mipush.sdk.MiPushMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static com.jyt.baseapp.api.Const.Event_NewArrive;
import static com.jyt.baseapp.api.Const.Event_StrangeArrive;


/**
 * @author LinWei on 2017/11/3 15:10
 */
public class HawkUtil {
    private static final String KEY_PSD = "KEY_SEARCH_HISTORY";
    private static final String KEY_PUSH= "KEY_PUSH";
    private static final String KEY_FOCUS= "KEY_FOCUS";
    private static final String KEY_FRIEND= "KEY_FRIEND";
    private static final String KEY_STRANGER= "KEY_STRANGER";

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

    public static void saveFocusList(List<UserBean> list){
        Hawk.put(KEY_FOCUS,list);
    }

    public static List<UserBean> getFocusList(){
        List<UserBean> onj= Hawk.get(KEY_FOCUS);
        if (onj==null){
            onj = new ArrayList<>();
        }
        return onj;
    }

    public static void addComMessage(Message message){
        List<UserBean> focusList = getFocusList();
        HashMap<Integer,FriendNewsBean> hashMap = Hawk.get(KEY_FRIEND);
        boolean isFriend = false;
        for(UserBean bean:focusList){
            if (String.valueOf(bean.getId()).equals(message.getSenderUserId())){
                //属于朋友
                if (hashMap == null){
                    hashMap = new HashMap<>();
                }
                FriendNewsBean friend = new FriendNewsBean(bean.getId(),bean.getHeadImg(),bean.getNickname(),System.currentTimeMillis());
                if (message.getContent() instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message.getContent();
                    friend.setContent(textMessage.getContent());
                }else if (message.getContent() instanceof ImageMessage){
                    friend.setContent("图片消息");
                }else if (message.getContent() instanceof VoiceMessage){
                    friend.setContent("音频消息");
                }

                hashMap.put(bean.getId(),friend);
                //消息类型为默认类型时触发监听，Tab2刷新消息数量
                EventBus.getDefault().post(new EventBean(Event_NewArrive));
                isFriend = true;
                break;
            }
        }
        Hawk.put(KEY_FRIEND,hashMap);
        //属于陌生人
        if (!isFriend){
            HashMap<Integer,FriendNewsBean> shash = Hawk.get(KEY_STRANGER);
            if (shash == null){
                shash = new HashMap<>();
            }
            FriendNewsBean friend = new FriendNewsBean();
            friend.setUserInfo(message.getContent().getUserInfo());
            if (message.getContent() instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message.getContent();
                friend.setContent(textMessage.getContent());
            }else if (message.getContent() instanceof ImageMessage){
                friend.setContent("图片消息");
            }else if (message.getContent() instanceof VoiceMessage){
                friend.setContent("音频消息");
            }
            shash.put(friend.getId(),friend);
            Hawk.put(KEY_STRANGER,shash);
            EventBus.getDefault().post(new EventBean(Event_StrangeArrive));

        }
    }

    public static List<FriendNewsBean> getStrangerList(){
        HashMap<Integer,FriendNewsBean> map = Hawk.get(KEY_STRANGER);
        List<FriendNewsBean> list = new ArrayList<>();
        if (map!=null){
            for (Map.Entry<Integer, FriendNewsBean> entry : map.entrySet()) {
                list.add( entry.getValue());
            }
        }
        return list;
    }

    public static List<FriendNewsBean> getFriendtList(){
        HashMap<Integer,FriendNewsBean> map = Hawk.get(KEY_FRIEND);
        List<FriendNewsBean> list = new ArrayList<>();
        if (map!=null){
            for (Map.Entry<Integer, FriendNewsBean> entry : map.entrySet()) {
                list.add( entry.getValue());
            }
        }
        return list;
    }


}
