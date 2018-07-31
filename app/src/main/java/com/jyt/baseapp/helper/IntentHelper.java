package com.jyt.baseapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.SearchBean;
import com.jyt.baseapp.bean.SearchConditionBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.util.FinishActivityManager;
import com.jyt.baseapp.view.activity.AccountActivity;
import com.jyt.baseapp.view.activity.AnswerActivity;
import com.jyt.baseapp.view.activity.AnswerAudienceActivity;
import com.jyt.baseapp.view.activity.AudienceActivity;
import com.jyt.baseapp.view.activity.AudienceVoiceActivity;
import com.jyt.baseapp.view.activity.BlackActivity;
import com.jyt.baseapp.view.activity.BrowseImagesActivity;
import com.jyt.baseapp.view.activity.CommunicationActivity;
import com.jyt.baseapp.view.activity.ContentActivity;
import com.jyt.baseapp.view.activity.DynamicActivity;
import com.jyt.baseapp.view.activity.EavesdropActivity;
import com.jyt.baseapp.view.activity.EndCallActivity;
import com.jyt.baseapp.view.activity.FFActivity;
import com.jyt.baseapp.view.activity.FeedbackActivity;
import com.jyt.baseapp.view.activity.LaunchActivity;
import com.jyt.baseapp.view.activity.ListActivity;
import com.jyt.baseapp.view.activity.LivePlayActivity;
import com.jyt.baseapp.view.activity.LivePlayVoiceActivity;
import com.jyt.baseapp.view.activity.MateActivity;
import com.jyt.baseapp.view.activity.ModifyActivity;
import com.jyt.baseapp.view.activity.ModifyTelActivity;
import com.jyt.baseapp.view.activity.NewsActivity;
import com.jyt.baseapp.view.activity.PersonActivity;
import com.jyt.baseapp.view.activity.PutForwardActivity;
import com.jyt.baseapp.view.activity.RechargeActivity;
import com.jyt.baseapp.view.activity.RecordingActivity;
import com.jyt.baseapp.view.activity.ReleaseActivity;
import com.jyt.baseapp.view.activity.SearchActivity;
import com.jyt.baseapp.view.activity.SearchResultActivity;
import com.jyt.baseapp.view.activity.SelImageActivity;
import com.jyt.baseapp.view.activity.SettingActivity;
import com.jyt.baseapp.view.activity.ThemeActivity;
import com.jyt.baseapp.view.activity.VideoActivity;
import com.jyt.baseapp.view.activity.WalletActivity;
import com.jyt.baseapp.view.activity.entrance.AuthenticationActivity;
import com.jyt.baseapp.view.activity.entrance.BindTelActivity;
import com.jyt.baseapp.view.activity.entrance.ExplainActivity;
import com.jyt.baseapp.view.activity.entrance.LoginActivity;
import com.jyt.baseapp.view.activity.entrance.PerfectInfoActivity;
import com.jyt.baseapp.view.activity.entrance.RegisterActivity;
import com.jyt.baseapp.view.activity.entrance.RetrievePwdActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2018/5/4 09:49
 */
public class IntentHelper {
    /**
     * 跳转到注册界面
     * @param activity
     */
    public static void OpenRegisterActivity(Activity activity){
        Intent intent = getIntent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转到登录界面
     * @param activity
     */
    public static void OpenLoginActivity(Activity activity){
        Intent intent = getIntent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转到登录界面
     * @param activity
     */
    public static void OpenLoginActivityByAuth(Activity activity){
        FinishActivityManager.getManager().finishActivity(ExplainActivity.class);
        activity.finish();
    }

    public static void OpenLoginActivityByLogOff(Context context){
        Intent intent = getIntent(context, LoginActivity.class);
        context.startActivity(intent);
        FinishActivityManager.getManager().finishAllActivity();
    }

    /**
     * 跳转完善资料界面
     * @param activity
     */
    public static void OpenPerfectInfoActivity(Activity activity,String tel,String vode,String pwd,String iode){
        Intent intent = getIntent(activity, PerfectInfoActivity.class);
        intent.putExtra(IntentKey.RegisterTel,tel);
        intent.putExtra(IntentKey.RegisterVode,vode);
        intent.putExtra(IntentKey.RegisterPwd,pwd);
        intent.putExtra(IntentKey.RegisterInviteCode,iode);
        activity.startActivity(intent);
    }

    /**
     * 跳转完善资料界面
     * @param activity
     */
    public static void OpenPerfectInfoActivityByQW(Activity activity,String QWId,boolean QOW){
        Intent intent = getIntent(activity, PerfectInfoActivity.class);
        intent.putExtra(IntentKey.RegisterISQW,true);
        intent.putExtra(IntentKey.RegisterQW,QWId);
        intent.putExtra(IntentKey.RegisterQOW,QOW);
        activity.startActivity(intent);
    }

    public static Tuple PerfectInfoActivityGetPara(Intent intent){
        String tel = intent.getStringExtra(IntentKey.RegisterTel);
        String vode = intent.getStringExtra(IntentKey.RegisterVode);
        String pwd = intent.getStringExtra(IntentKey.RegisterPwd);
        String iode = intent.getStringExtra(IntentKey.RegisterInviteCode);
        String QWId = intent.getStringExtra(IntentKey.RegisterQW);
        boolean isQW = intent.getBooleanExtra(IntentKey.RegisterISQW,false);
        boolean QOW = intent.getBooleanExtra(IntentKey.RegisterQOW,false);
        if (isQW){
            return new Tuple(isQW,QWId,QOW);
        }
        return new Tuple(isQW,tel,vode,pwd,iode);
    }

    /**
     * 跳转女用户说明界面
     * @param activity
     */
    public static void OpenExplainActivity(Activity activity,String RID){
        Intent intent = getIntent(activity, ExplainActivity.class);
        intent.putExtra(IntentKey.RegisterRID,Integer.valueOf(RID));
        activity.startActivity(intent);
        FinishActivityManager manager =FinishActivityManager.getManager();
        if(manager.IsActivityExist(RegisterActivity.class)){
            manager.finishActivity(RegisterActivity.class);
        }
        if(manager.IsActivityExist(PerfectInfoActivity.class)){
            manager.finishActivity(PerfectInfoActivity.class);
        }
        if(manager.IsActivityExist(BindTelActivity.class)){
            manager.finishActivity(BindTelActivity.class);
        }
    }

    public static Tuple ExplainActivityGetPara(Intent intent){
        int rid = intent.getIntExtra(IntentKey.RegisterRID,0);
        return new Tuple(rid);
    }



    /**
     * 跳转验证界面
     * @param activity
     */
    public static void OpenAuthenticationActivity(Activity activity,int RID,int code){
        Intent intent = getIntent(activity, AuthenticationActivity.class);
        intent.putExtra(IntentKey.RegisterRID,RID);
        intent.putExtra(IntentKey.RegisterCode,code);
        activity.startActivity(intent);
    }

    public static Tuple AuthenticationActivityGetPara(Intent intent){
        int rid = intent.getIntExtra(IntentKey.RegisterRID,0);
        int code = intent.getIntExtra(IntentKey.RegisterCode,0);
        return new Tuple(rid,code);
    }



    /**
     * 手机号码绑定界面-第三方登录专用
     * @param activity
     * @param QWId
     * @param nick
     * @param birthday
     * @param city
     * @param sex
     */
    public static void OpenBindTelActivity(Activity activity,String QWId,String nick,String birthday,String city,int sex,boolean QOW){
        Intent intent = getIntent(activity, BindTelActivity.class);
        intent.putExtra(IntentKey.RegisterQW,QWId);
        intent.putExtra(IntentKey.RegisterNick,nick);
        intent.putExtra(IntentKey.RegisterBirthday,birthday);
        intent.putExtra(IntentKey.RegisterCity,city);
        intent.putExtra(IntentKey.RegisterSex,sex);
        intent.putExtra(IntentKey.RegisterQOW,QOW);
        activity.startActivity(intent);
    }

    public static Tuple BindTelActivityGetPara(Intent intent){
        String id = intent.getStringExtra(IntentKey.RegisterQW);
        String nick = intent.getStringExtra(IntentKey.RegisterNick);
        String birthday = intent.getStringExtra(IntentKey.RegisterBirthday);
        String city = intent.getStringExtra(IntentKey.RegisterCity);
        int sex = intent.getIntExtra(IntentKey.RegisterSex,0);
        boolean QOW = intent.getBooleanExtra(IntentKey.RegisterQOW,false);
        return new Tuple(id,nick,birthday,city,sex,QOW);
    }


    /**
     * 跳转忘记密码界面
     * @param activity
     */
    public static void OpenRetrievePwdActivity(Activity activity){
        Intent intent = new Intent(activity, RetrievePwdActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转主界面
     * @param activity
     */
    public static void OpenContentActivity(Activity activity){
        Intent intent = getIntent(activity, ContentActivity.class);
        activity.startActivity(intent);
        FinishActivityManager manager =FinishActivityManager.getManager();
        activity.finish();

    }







    /**
     * 打开相册
     * @param context
     * @param maxSelCount
     * @param images
     */
    public static void openSelImageActivityForResult(Object context, int maxSelCount,List images){
        if(images == null){
            images = new ArrayList();
        }

        if (context instanceof  Activity){
            Intent intent = getIntent((Context) context, SelImageActivity.class);
            intent.putExtra(IntentKey.MAX_COUNT,maxSelCount);
            intent.putStringArrayListExtra(IntentKey.IMAGES, (ArrayList<String>) images);
            ((Activity) context).startActivityForResult(intent,IntentRequestCode.CODE_SEL_IMAGES);
        }else if (context instanceof Fragment){
            Intent intent = getIntent(((Fragment) context).getContext(), SelImageActivity.class);
            intent.putExtra(IntentKey.MAX_COUNT,maxSelCount);
            intent.putStringArrayListExtra(IntentKey.IMAGES, (ArrayList<String>) images);
            ((Fragment) context).startActivityForResult(intent,IntentRequestCode.CODE_SEL_IMAGES);
        }
    }

    /**
     *
     * @param intent
     * @return int maxCount,List images
     */
    public static Tuple SelImageActivityGetPara(Intent intent){
        int maxCount = intent.getIntExtra(IntentKey.MAX_COUNT,0);
        List images = intent.getStringArrayListExtra(IntentKey.IMAGES);
        return new Tuple(maxCount,images);
    }

    /**
     *
     * @param intent
     * @return List images
     */
    public static Tuple SelImageActivityGetResult(Intent intent){
        List images = intent.getStringArrayListExtra(IntentKey.IMAGES);
        return new Tuple(images);
    }
    public static void SelImageActivitySetResult(Activity activity,int resultCode,List images){
        Intent intent = getIntent();
        intent.putStringArrayListExtra(IntentKey.IMAGES, (ArrayList<String>) images);
        activity.setResult(resultCode,intent);
        activity.finish();
    }


    /**
     * 跳转到图片浏览
     * @param context
     * @param images 图片资源
     * @param startIndex 浏览的起始位置
     */
    public static void openBrowseImagesActivity(Context context, List<String> images, int startIndex){
        Intent intent = getIntent(context, BrowseImagesActivity.class);
        intent.putExtra(IntentKey.START_INDEX,startIndex);
        intent.putStringArrayListExtra(IntentKey.IMAGES, (ArrayList<String>) images);
        context.startActivity(intent);
    }

    /**
     * 浏览图片 By Path
     * @param context
     * @param image
     */
    public static void openBrowseImagesActivity(Context context ,String image){
        List images = new ArrayList();
        images.add(image);
        openBrowseImagesActivity(context,images,0);
    }

    /**
     * 浏览图片 By Uri
     * @param context
     * @param uri
     */
    public static void openBrowseImagesActivity(Context context ,Uri uri){
        List images = new ArrayList();
        images.add(uri);
        openBrowseImagesActivity(context,images,0);
    }

    public static Tuple BrowseImagesActivityGetPara(Intent intent){
        List list = intent.getStringArrayListExtra(IntentKey.IMAGES);
        int startIndex = intent.getIntExtra(IntentKey.START_INDEX,0);
        return new Tuple(list,startIndex);
    }

    /**
     * 打开搜索界面
     * @param activity
     */
    public static void OpenSearchActivity(Activity activity){
        Intent intent =getIntent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 打开搜索结果界面
     * @param activity
     */
    public static void OpenSearchResultActivity(Activity activity, SearchBean data , SearchConditionBean condition){
        Intent intent =getIntent(activity, SearchResultActivity.class);
        intent.putExtra(IntentKey.SearchResult, data);
        intent.putExtra(IntentKey.SearchCondition, condition);
        activity.startActivity(intent);
    }

    public static Tuple SearchResultGetPara(Intent intent){
        SearchBean data = (SearchBean) intent.getSerializableExtra(IntentKey.SearchResult);
        SearchConditionBean condition = (SearchConditionBean) intent.getSerializableExtra(IntentKey.SearchCondition);
        return new Tuple(data,condition);
    }

    /**
     * 跳转列表界面（多个）
     * @param activity
     * @param PageCode
     */
    public static void OpenListActivity(Activity activity,int PageCode){
        Intent intent =getIntent(activity, ListActivity.class);
        intent.putExtra(IntentKey.TAB1_PAGE_CODE,PageCode);
        activity.startActivity(intent);
    }
    public static Tuple ListActivityGetPara(Intent intent){
        int code = intent.getIntExtra(IntentKey.TAB1_PAGE_CODE,0);
        return new Tuple(code);
    }

    /**
     * 跳转主题界面
     * @param activity
     */
    public static void OpenThemeActivity(Activity activity){
        Intent intent =getIntent(activity, ThemeActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转动态界面
     * @param activity
     */
    public static void OpenDynamicActivity(Activity activity){
        Intent intent =getIntent(activity, DynamicActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 打开动态的发布界面
     * @param activity
     */
    public static void OpenReleaseActivityForResult(Activity activity){
        Intent intent =getIntent(activity, ReleaseActivity.class);
        activity.startActivityForResult(intent,IntentRequestCode.CODE_RELEASE);
    }

    /**
     * 跳转设置界面
     * @param activity
     */
    public static void OpenSettingActivity(Activity activity){
        Intent intent =getIntent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转编辑界面
     * @param activity
     */
    public static void OpenModifyActivity(Activity activity){
        Intent intent =getIntent(activity, ModifyActivity.class);
        activity.startActivityForResult(intent,IntentRequestCode.CODE_MODIFY);
    }

    /**
     * 主播收到观众电话，跳转接听界面
     * @param context
     */
    public static void OpenAnswerActivity(Context context,String name ,String hpic , boolean isVoice){
        Intent intent =getIntent(context, AnswerActivity.class);
        intent.putExtra(IntentKey.Answer_Name,name);
        intent.putExtra(IntentKey.Answer_Hpic,hpic);
        intent.putExtra(IntentKey.Answer_IsVoice,isVoice);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Tuple AnswerActivityGetPara(Intent intent){
        String name = intent.getStringExtra(IntentKey.Answer_Name);
        String hpic = intent.getStringExtra(IntentKey.Answer_Hpic);
        boolean isVoice = intent.getBooleanExtra(IntentKey.Answer_IsVoice,false);
        return new Tuple(name,hpic,isVoice);
    }

    /**
     * 观众收到主播回拨电话，跳转接听界面
     * @param activity
     */
    public static void OpenAnswerAudienceActivity(Context activity,String name ,String hpic , String uid , boolean isVoice){
        Intent intent =getIntent(activity, AnswerAudienceActivity.class);
        intent.putExtra(IntentKey.Answer_Name,name);
        intent.putExtra(IntentKey.Answer_Hpic,hpic);
        intent.putExtra(IntentKey.Answer_Id,uid);
        intent.putExtra(IntentKey.Answer_IsVoice,isVoice);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static Tuple AnswerActivityAudienceGetPara(Intent intent){
        String name = intent.getStringExtra(IntentKey.Answer_Name);
        String hpic = intent.getStringExtra(IntentKey.Answer_Hpic);
        String uid = intent.getStringExtra(IntentKey.Answer_Id);
        boolean isVoice = intent.getBooleanExtra(IntentKey.Answer_IsVoice,false);
        return new Tuple(name,hpic,uid,isVoice);
    }


    /**
     * 跳转黑名单
     * @param activity
     */
    public static void OpenBlackActivity(Activity activity){
        Intent intent =getIntent(activity, BlackActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转黑名单
     * @param activity
     */
    public static void OpenModifyTelActivity(Activity activity){
        Intent intent =getIntent(activity, ModifyTelActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转
     * @param activity
     */
    public static void OpenFFActivit(Activity activity,int page){
        Intent intent =getIntent(activity, FFActivity.class);
        intent.putExtra(IntentKey.FFPage,page);
        activity.startActivity(intent);
    }

    public static Tuple FFActivityGetPara(Intent intent){
        int page = intent.getIntExtra(IntentKey.FFPage,0);
        return new Tuple(page);
    }

    /**
     * 跳转到直播界面
     * @param activity
     */
    public static void OpenLivePlayActivity(Activity activity){
        Intent intent =getIntent(activity, LivePlayActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转到直播界面
     * @param context
     */
    public static void OpenLivePlayActivity(Context context){
        Intent intent =getIntent(context, LivePlayActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转到通话界面
     * @param context
     */
    public static void OpenLivePlayVoiceActivity(Context context){
        Intent intent =getIntent(context, LivePlayVoiceActivity.class);
        context.startActivity(intent);
    }



    /**
     * 跳转到直播界面
     * @param activity
     */
    public static void OpenAudienceActivity(Context activity){
        Intent intent =getIntent(activity, AudienceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 跳转到观众声音界面
     * @param activity
     */
    public static void OpenAudienceVoiceActivity(Context activity){
        Intent intent =getIntent(activity, AudienceVoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    /**
     * 打开拨打电话界面
     * @param activity
     * @param id
     * @param type
     * @param nick
     * @param hpic
     */
    public static void OpenLaunchActivity(Activity activity , int id ,int type,String nick ,String hpic){
        Intent intent =getIntent(activity, LaunchActivity.class);
        intent.putExtra(IntentKey.ComId,id);
        intent.putExtra(IntentKey.Type,type);
        intent.putExtra(IntentKey.RegisterNick,nick);
        intent.putExtra(IntentKey.IMAGES,hpic);
        activity.startActivity(intent);
    }

    public static Tuple LaunchActivityGetPara(Intent intent){
        int id = intent.getIntExtra(IntentKey.ComId,0);
        int type = intent.getIntExtra(IntentKey.Type,0);
        String nick  = intent.getStringExtra(IntentKey.RegisterNick);
        String hpic  = intent.getStringExtra(IntentKey.IMAGES);
        return new Tuple(id,type,nick,hpic);
    }

    /**
     * 跳转偷听界面
     * @param activity
     */
    public static void OpenEavesdropActivity(Activity activity){
        Intent intent = getIntent(activity, EavesdropActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 打开随机拨打电话界面
     * @param activity
     */
    public static void OpenMateActivity(Activity activity){
        Intent intent = getIntent(activity, MateActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转通话结束界面 用于聊天双方使用
     * @param activity
     * @param isLive
     */
    public static void OpenEndCallActivity(Activity activity , boolean isLive){
        Intent intent = getIntent(activity, EndCallActivity.class);
        intent.putExtra(Const.IsLive,isLive);
        intent.putExtra(Const.FinishTime,"");
        activity.startActivity(intent);
    }

    /**
     * 跳转通话结束界面 用于聊天双方使用
     * @param activity
     * @param isLive
     */
    public static void OpenEndCallActivity(Context activity , boolean isLive){
        Intent intent = getIntent(activity, EndCallActivity.class);
        intent.putExtra(Const.IsLive,isLive);
        intent.putExtra(Const.FinishTime,"");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 跳转通话结束界面，用于偷听观众使用
     * @param activity
     * @param time
     */
    public static void OpenEndCallActivity(Activity activity ,String time){
        Intent intent = getIntent(activity, EndCallActivity.class);
        intent.putExtra(Const.IsLive,false);
        intent.putExtra(Const.FinishTime,time);
        activity.startActivity(intent);
    }

    /**
     * 跳转通话结束界面，用于偷听观众使用
     * @param activity
     * @param time
     */
    public static void OpenEndCallActivity(Context activity ,String time){
        Intent intent = getIntent(activity, EndCallActivity.class);
        intent.putExtra(Const.IsLive,false);
        intent.putExtra(Const.FinishTime,time);
        activity.startActivity(intent);
    }

    public static Tuple EndCallActivityGetPara(Intent intent){
        boolean isLive = intent.getBooleanExtra(Const.IsLive,false);
        String time = intent.getStringExtra(Const.FinishTime);
        return new Tuple(isLive,time);
    }










    /**
     * 跳转到录制小视频界面
     * @param activity
     */
    public static void OpenRecordingActivity(Activity activity){
        Intent intent = getIntent(activity, RecordingActivity.class);
        activity.startActivityForResult(intent,IntentRequestCode.CODE_RECORDING);
    }

    public static void RecordingActivitySetResult(Activity activity,int resultCode,String path){
        Intent intent = getIntent();
        intent.putExtra(IntentKey.VIDEO_PATH,path);
        activity.setResult(resultCode,intent);
        activity.finish();
    }

    public static Tuple RecordingActivityGetPara(Intent intent){
        if (intent!=null){
            String path = intent.getStringExtra(IntentKey.VIDEO_PATH);
            return new Tuple(path);
        }
       return null;
    }

    /**
     * 跳转到视频播放界面
     * @param activity
     * @param path
     */
    public static void OpenVideoActivity(Activity activity,String path){
        Intent intent = getIntent(activity, VideoActivity.class);
        intent.putExtra(IntentKey.VIDEO_PATH,path);
        activity.startActivity(intent);
    }

    public static Tuple VideoActivityGetPara(Intent intent){
        String path = intent.getStringExtra(IntentKey.VIDEO_PATH);
        return new Tuple(path);
    }

    /**
     * 跳转到钱包界面
     * @param activity
     */
    public static void OpenWalletActivity(Activity activity){
        Intent intent =getIntent(activity, WalletActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转到充值界面
     * @param activity
     */
    public static void OpenRechargeActivity(Activity activity){
        Intent intent =getIntent(activity, RechargeActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转到提现界面
     * @param activity
     */
    public static void OpenPutForwardActivity(Activity activity){
        Intent intent =getIntent(activity, PutForwardActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转到明细界面
     * @param activity
     */
    public static void OpenAccountActivity( Activity activity){
        Intent intent =getIntent(activity, AccountActivity.class);

        activity.startActivity(intent);
    }


    /**
     * 跳转到通讯界面
     * @param activity
     */
    public static void OpenCommunicationActivity(int id,Activity activity){
        Intent intent =getIntent(activity, CommunicationActivity.class);
        intent.putExtra(IntentKey.ComId,id);
        activity.startActivity(intent);
    }

    public static Tuple CommunicationActivityGetPara(Intent intent){
        int id = intent.getIntExtra(IntentKey.ComId,-1);
        return new Tuple(id);
    }

    /**
     * 跳转到明反馈界面
     * @param activity
     */
    public static void OpenFeedBackActivity(Activity activity){
        Intent intent =getIntent(activity, FeedbackActivity.class);
        activity.startActivity(intent);
    }


    /**
     * 跳转到消息界面（多个）
     * @param activity
     * @param stateCode
     */
    public static void OpenNewActivity(Activity activity,int stateCode){
        Intent intent =getIntent(activity, NewsActivity.class);
        intent.putExtra(IntentKey.NEW_HOLDER_STATE,stateCode);
        activity.startActivity(intent);
    }
    public static Tuple NewActivityGetPara(Intent intent){
        int code = intent.getIntExtra(IntentKey.NEW_HOLDER_STATE,0);
        return new Tuple(code);
    }

    /**
     * 跳转其他用户详情页
     * @param activity
     * @param id
     */
    public static void OpenPersonActivity(Activity activity,int id){
        Intent intent =getIntent(activity, PersonActivity.class);
        intent.putExtra(IntentKey.User_ID,id);
        activity.startActivity(intent);
    }
    public static Tuple PersonActivityGetPara(Intent intent){
        int id = intent.getIntExtra(IntentKey.User_ID,0);
        return new Tuple(id);
    }


    public static Intent getIntent(){
        return new Intent();
    }

    public static Intent getIntent(Context context, Class activityClass){
        return new Intent(context,activityClass);
    }




}
