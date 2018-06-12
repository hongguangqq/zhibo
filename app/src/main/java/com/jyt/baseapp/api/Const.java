package com.jyt.baseapp.api;

import android.content.Context;
import android.os.Environment;

import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.util.BaseUtil;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;

import io.rong.imlib.RongIMClient;

/**
 * @author LinWei on 2018/5/14 11:35
 */
public class Const {
    //网易云
    public static final String WangYi = "http://asset.nos-eastchina1.126.net/";
    public static final String WyAccessKey = "2a7c38e666cf4079a0dd0ffe66e3dfb5";
    public static final String WySecretKey = "d2299bf5e9584583ab4a52c216f2f78a";
    public static final String WyMainFile =WangYi + "test/" + Const.getUserID() + "/";
    //小米推送
    public static final String MiAppID = "2882303761517790587";
    public static final String MiAppKey = "5581779020587";
    //文件主目录
    public final static String mMainFile = Environment.getExternalStorageDirectory().getPath() + File.separator + "zhibo";
    //录音成功
    public static final int RECORD_SUCCESS = 100;
    //录音失败
    public static final int RECORD_FAIL = 101;
    //录音时间太短
    public static final int RECORD_TOO_SHORT = 102;
    //安卓6.0以上手机权限处理
    public static final int PERMISSIONS_REQUEST_FOR_AUDIO = 1;
    //播放完成
    public static final int PLAY_COMPLETION = 103;
    //播放错误
    public static final int PLAY_ERROR = 104;
    //取消录制
    public static final int RECORD_CANCEL = 105;

    //用户ID
    public static final String UserID = "UserID";
    //用户名
    public static final String UserName = "UserName";
    //昵称
    public static final String UserNick = "UserNick";
    //关注
    public static final String UserAttention = "UserAttention";
     //粉丝
    public static final String UserFans = "UserFans";
    //Token
    public static final String UserToken = "UserToken";
    //在线状态
    public static final String UserOnLineState = "UserOnLineState";
    //认证状态
    public static final String UserAnchorState = "UserAnchorState";
    //头像
    public static final String UserHeadImg = "UserHeadImg";
    //性别
    public static final String UserGender = "UserGender";
    //MiPush
    public static final String MiPush = "MiPush";

    public static final String Reciver_CODE1="Reciver_CODE1";

    public static final String RongYunKey="cpj2xarlc78zn";
    public static final String RongYunSecret="HXsxYcwy88";


    public static void SaveUser(UserBean user){

        BaseUtil.setSpString(UserID,user.getId()+"");
        BaseUtil.setSpString(UserName,user.getNickname());
        BaseUtil.setSpString(UserNick,user.getNickname());
        BaseUtil.setSpString(UserToken,user.getToken());
        BaseUtil.setSpNumInt(UserOnLineState,user.getOnlineState());
        BaseUtil.setSpNumInt(UserAnchorState,user.getAnchorState());
        BaseUtil.setSpString(UserHeadImg,user.getHeadImg());
        BaseUtil.setSpNumInt(UserGender,user.getGender());
        BaseUtil.setSpString(MiPush,user.getMiPush());
    }

    public static void LogOff(Context context){
        BaseUtil.setSpString(UserID,"");
        BaseUtil.setSpString(UserName,"");
        BaseUtil.setSpString(UserNick,"");
        BaseUtil.setSpString(UserToken,"");
        BaseUtil.setSpString(UserAnchorState,"");
        BaseUtil.setSpString(MiPush,"");
        MiPushClient.unregisterPush(BaseUtil.getContext());
        IntentHelper.OpenLoginActivityByLogOff(context);
        BaseUtil.makeText("退出登录成功");
        RongIMClient.getInstance().disconnect();
    }


    public static String getUserID(){
        return BaseUtil.getSpString(Const.UserID);
    }

    public static String getUserNick(){
        return BaseUtil.getSpString(Const.UserNick);
    }

    public static String getUserToken(){
        return BaseUtil.getSpString(UserToken);
    }

    public static int getOnLineState(){
        return BaseUtil.getSpInt(UserOnLineState);
    }

    public static int getAnchorState(){
        return BaseUtil.getSpInt(UserAnchorState);
    }

    public static void setUserOnLineState(int Online){
        BaseUtil.setSpNumInt(UserOnLineState,Online);
    }

    public static int getGender(){
        return BaseUtil.getSpInt(UserGender);
    }

    public static String getUserHeadImg(){
        String img = BaseUtil.getSpString(UserHeadImg);
        if (img==null){
            img = "";
        }
        return img;
    }






}
