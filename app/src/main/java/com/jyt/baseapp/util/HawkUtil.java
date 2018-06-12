package com.jyt.baseapp.util;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2017/11/3 15:10
 */
public class HawkUtil {
    private static final String KEY_PSD = "KEY_SEARCH_HISTORY";

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


}
