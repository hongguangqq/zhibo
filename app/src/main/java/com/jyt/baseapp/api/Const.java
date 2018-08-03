package com.jyt.baseapp.api;

import android.content.Context;
import android.os.Environment;

import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LoginModel;
import com.jyt.baseapp.util.BaseUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
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
    //参加的活动ID
    public static final String MyJoinActivityId = "MyJoinActivityId";

    public static final String Reciver_Tab1 ="Reciver_Tab1";
    public static final String Reciver_Message="Reciver_Message";


    public static final String RongToken="RongToken";
    public static final String RongYunKey="cpj2xarlc78zn";
    public static final String RongYunSecret="HXsxYcwy88";
    public static final String Rong_Message="Rong_Message";

    public static final String WyAccount = "WyAccount";
    public static final String WyToken = "WyToken";

    public static final String IsLive = "IsLive";//endcall界面判断是否为主播
    public static final String FinishTime = "FinishTime";//结束时间
    public static final String Event_Launch = "Event_Launch";
    public static final String Event_Live = "Event_Launch";
    public static final String Event_Audience = "Event_Audience";
    public static final String Event_HangUp = "Event_HangUp";
    public static final String Event_LiveHangUp = "Event_LiveHangUp";//主播拒听
    public static final String Event_UserJoin = "Event_UserJoin";//用户加入
    public static final String Event_UserLeave = "Event_UserLeave";//用户离开
    public static final String Event_NewArrive = "Event_NewArrive";//好友消息到达
    public static final String Event_StrangeArrive = "Event_StrangeArrive";//陌生人消息到达
    public static final String Event_SystemFirst = "Event_SystemFirst";//第一条系统消息
    public static final String Event_NewUpData = "Event_NewUpData";//关注/未关注消息重新排列


    public static void SaveUser(UserBean user , LoginModel loginModel){

        BaseUtil.setSpString(UserID,user.getId()+"");
        BaseUtil.setSpString(UserName,user.getNickname());
        BaseUtil.setSpString(UserNick,user.getNickname());
        BaseUtil.setSpString(UserToken,user.getToken());
        BaseUtil.setSpNumInt(UserOnLineState,user.getOnlineState());
        BaseUtil.setSpNumInt(UserAnchorState,user.getAnchorState());
        BaseUtil.setSpString(UserHeadImg,user.getHeadImg());
        BaseUtil.setSpNumInt(UserGender,user.getGender());
        BaseUtil.setSpString(MiPush,user.getMiPush());
        BaseUtil.setSpString(WyAccount,user.getEasyId());
        BaseUtil.setSpString(WyToken,user.getEasyToken());
        BaseUtil.setSpNumInt(MyJoinActivityId,user.getActivityId());
        MiPushClient.setAlias(BaseUtil.getContext(), user.getId()+"",null);
        MiPushClient.setUserAccount(BaseUtil.getContext(), user.getId()+"",null);





    }

    public static void LogOff(Context context){
        BaseUtil.setSpString(UserID,"");
        BaseUtil.setSpString(UserName,"");
        BaseUtil.setSpString(UserNick,"");
        BaseUtil.setSpString(UserToken,"");
        BaseUtil.setSpString(UserAnchorState,"");
        BaseUtil.setSpString(MiPush,"");
        BaseUtil.setSpString(RongToken,"");
        BaseUtil.setSpString(WyAccount,"");
        BaseUtil.setSpString(WyToken,"");
        MiPushClient.unsetUserAccount(context,Const.getUserID(),null);
//        MiPushClient.unregisterPush(BaseUtil.getContext());
//        MiPushClient.pausePush(BaseUtil.getContext(),null);
        BaseUtil.makeText("退出登录成功");
        RongIMClient.getInstance().logout();
        NIMClient.getService(AuthService.class).logout();
        IntentHelper.OpenLoginActivityByLogOff(context);
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

    public static int getMyActivityId(){
        return BaseUtil.getSpInt(MyJoinActivityId);
    }

    public static int getGender(){
        return BaseUtil.getSpInt(UserGender);
    }

    public static String getRongToken(){
        return BaseUtil.getSpString(RongToken);
    }

    public static String getWyAccount(){
        return BaseUtil.getSpString(WyAccount);
    }

    public static String getWyToken(){
        return BaseUtil.getSpString(WyToken);
    }

    public static String getUserHeadImg(){
        String img = BaseUtil.getSpString(UserHeadImg);
        if (img==null){
            img = "";
        }
        return img;
    }






}
