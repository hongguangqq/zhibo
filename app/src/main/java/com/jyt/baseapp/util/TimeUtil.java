package com.jyt.baseapp.util;

/**
 * @author LinWei on 2018/7/11 10:30
 */
public class TimeUtil {
    /**
     * 根据毫秒返回分秒
     * @param time
     * @return
     */
    public static String getFormatMS(long time){
        time=time/1000;//总秒数
        int s= (int) (time%60);//秒
        int m= (int) (time/60);//分
        return String.format("%02d:%02d",m,s);
    }

    /**
     * 根据毫秒返回时分秒
     * @param time
     * @return
     */
    public static String getFormatHMS(long time){
        time=time/1000;//总秒数
        int s= (int) (time%60);//秒
        int m= (int) (time/60);//分
        int h=(int) (time/3600);//时
        return String.format("%02d:%02d:%02d",h,m,s);
    }

    public static String getTimeSlot(long time){
        String s = null;
        if (time<60){
            s=time+"分钟前";
        }else if (time>=60 && time<24*60){
            s=(time/60)+"小时前";
        }else if (time>=24*60){
            s=(time/(24*60))+"天前";
        }
        return s;
    }


}
