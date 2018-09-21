package com.jyt.baseapp.api;

/**
 * @author LinWei on 2018/5/24 15:59
 */
public class Path {
    public static final String BasePath = "http://192.168.3.128:8999";
//    public static final String BasePath = "http://119.23.66.37:8999";
    public static final String TelRegister = BasePath+"/chat/user/telRegister";
    public static final String QWRegister = BasePath+"/chat/user/WXQQRegister";
    public static final String GetVerification = BasePath+"/chat/user/getVerification";
    public static final String AuthInfoData = BasePath+"/chat/user/realName";
    public static final String Login = BasePath+"/chat/user/login";
    public static final String LogOff = BasePath+"/chat/user/logout";
    public static final String GetBunner = BasePath+"/chat/user/getBunner";
    public static final String GetAllList = BasePath+"/chat/user/getAll";
    public static final String GetThemeList = BasePath+"/chat/user/getActivityList";
    public static final String EditOnlineState = BasePath+"/chat/user/editOnlineState";
    public static final String GetInOut = BasePath+"/chat/user/inOut";
    public static final String ForgetPwd = BasePath+"/chat/user/forgetPwd";
    //搜寻，也用于用户列表获取
    public static final String SearchKey = BasePath+"/chat/user/indexSearch";
    public static final String GetFocus = BasePath+"/chat/user/meFollowList";
    public static final String GetRecommend = BasePath+"/chat/user/getRecommandList";
    public static final String GetHot = BasePath+"/chat/statistical/getlist";
    public static final String GetBalance = BasePath+"/chat/user/getSelfBlance";
    public static final String GetFollowCount = BasePath+"/chat/user/getMeFollowCount";
    public static final String GetFansCount = BasePath+"/chat/user/getFollowMeCount";
    public static final String GetWalletAccount = BasePath+"/chat/user/getWalletDetail";
    public static final String ReleaseDynamic = BasePath+"/chat/user/insertTrends";
    public static final String GetDynamic = BasePath+"/chat/user/getFriendTrends";
    public static final String LikeZang = BasePath+"/chat/user/likeTrends";
    public static final String DisZang = BasePath+"/chat/user/disLikeTrends";

    public static final String GetMyUserData = BasePath+"/chat/user/getMySelf";
    public static final String GetOtherUserData = BasePath+"/chat/user/userDetail";
    public static final String GetOtherUserData2 = BasePath+"/chat/user/userOne";
    public static final String UpDateMyData = BasePath+"/chat/user/saveEdit";
    public static final String ToFollow = BasePath+"/chat/user/follow";
    public static final String CancelFollow = BasePath+"/chat/user/disFollow";
    public static final String PullBack = BasePath+"/chat/user/black";
    public static final String ReportUser = BasePath+"/chat/user/report";
    public static final String SetPrice = BasePath+"/chat/user/setUserPrice";
    public static final String BlackList = BasePath+"/chat/user/getMyBlackList";
    public static final String DeleteBlackList = BasePath+"/chat/user/deleteMyBlack";
    public static final String ModifyPhone = BasePath+"/chat/user/changeBind";
    public static final String JoinActivity = BasePath+"/chat/activity/joinActivity";
    public static final String GetActivityUser = BasePath+"/chat/activity/getActivityUser";
    public static final String AliPay = BasePath+"/chat/user/aliPay";
    public static final String GetFollowList = BasePath+"/chat/user/meFollowList";
    public static final String GetFansList = BasePath+"/chat/user/followMeList";
    public static final String FeedBack = BasePath+"/chat/user/saveSuggestion";
    public static final String RongYunPath = "http://api.cn.ronghub.com/user/getToken.json";
    public static final String InsertAppointment = BasePath + "/chat/sub/insertAppointment";
    public static final String MaleAppointmentList = BasePath + "/chat/sub/userAppointmentList";
    public static final String FemaleAppointmentList = BasePath + "/chat/sub/anchorAppointmentList";
    public static final String DeleteAppoint = BasePath + "/chat/sub/deleteAppoint";
    public static final String MakeCall = BasePath + "/chat/user/makeCall";
    public static final String HangUp = BasePath + "/chat/user/hangUp";
    public static final String DongHangUp = BasePath + "/chat/user/doneHangUp";
    public static final String RingBack = BasePath+"/chat/user/ringBack";
    public static final String SendGift = BasePath+"/chat/user/sendGift";
    public static final String AnchorAnswer = BasePath + "/chat/user/anchorAnswer";
    public static final String RandomRing = BasePath + "/chat/user/randomRing";
    public static final String EavesdropLive = BasePath + "/chat/user/eavesdrop";
    public static final String GetEavesdropNum = BasePath + "/chat/user/nowListen";
    public static final String GetComMoney = BasePath + "/chat/user/finishTalk";
    public static final String ReportProgressTime = BasePath + "/chat/user/userGetNowBlance";
    public static final String GetBarrageList = BasePath + "/chat/user/danmu";
    public static final String SendBarrage = BasePath + "/chat/user/sendDanmu";
    public static final String GetUserBlance = BasePath + "/chat/user/getLookBlance";
    public static final String GetLiveBlance = BasePath + "/chat/user/getSelfBlance";
    public static final String GetBarrageGiftList = BasePath +"/chat/gift/getGiftList";
    public static final String GetVistorNum = BasePath+"/chat/user/countWhoLookMe";//查看时间段内谁看过我用户数量
    public static final String GetVistor = BasePath+"/chat/user/whoLookMe";//查看时间段内谁看过我用户数据
    public static final String GetFocusID = BasePath+"/chat/user/friendMsg";//获取好友ID列表
    public static final String ChangeAppointeState = BasePath+"/chat/sub/changeState";//获取好友ID列表
    public static final String GetChargeData = BasePath+"/chat/charge/getPageCharge";//获取充值记录
    public static final String PutForward = BasePath+"/chat/widthDraw/add";//提现

}
