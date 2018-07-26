package com.jyt.baseapp.receiver;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HawkUtil;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * @author LinWei on 2018/6/1 14:56
 */
public class DemoMessageReceiver extends PushMessageReceiver {
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
//    private static final String MSG_Live = "1";
//    private static final String MSG_Audience = "2";
//    private static final String MSG_HangUp = "3";//用户挂断
//    private static final String MSG_LiveHangUp = "4";//主播挂断
//    private static final String MSG_BarrageText = "5";//文字弹幕
//    private static final String MSG_BarrageImg = "6";//图片弹幕
//    private static final String MSG_LiveVoice = "7";//主播音频来电
//    private static final String MSG_AudienceVoice = "8";//观众音频来电


    //透传消息到达客户端时调用
    //作用：可通过参数message从而获得透传消息，具体请看官方SDK文档
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

        //打印消息方便测试
        BaseUtil.e("透传消息到达了");
        BaseUtil.e("透传消息是"+message.toString());
        Map<String,String> map = message.getExtra();
        String code = map.get("code");
//        if (MSG_Live.equals(code)){
//            //主播收到观众开播请求，进入直播界面，创建房间
//            String jobjStr = map.get("message");
//            try {
//                JSONObject jobj = new JSONObject(jobjStr);
//                ScannerManager.comID = jobj.getString("userAccid");
//                ScannerManager.trId = jobj.getString("trId");
//                String name = jobj.getString("userName");
//                String hpic = jobj.optString("img");
//                IntentHelper.OpenAnswerActivity(context,name,hpic,false);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if (MSG_Audience.equals(code)){
//            //观众拨打电话给主播，主播同意后进入直播界面
//            String jobjStr = map.get("message");
//            try {
//                JSONObject jobj = new JSONObject(jobjStr);
//                ScannerManager.comID = jobj.getString("accid");
//                ScannerManager.mMeetingName = jobj.getString("roomName");
//                IntentHelper.OpenAudienceActivity(context);
//                EventBus.getDefault().post(new EventBean(Const.Event_Launch));//拨打电话界面销毁
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if (MSG_HangUp.equals(code)){
//            //挂电话
//            if (FinishActivityManager.getManager().IsActivityExist(LivePlayActivity.class)){
//                EventBus.getDefault().post(new EventBean(Const.Event_HangUp));
//            }
//
//        } else if (MSG_LiveHangUp.equals(code)){
//
//        } else if (MSG_BarrageText.equals(code)){
//            String jobjStr = map.get("message");
//            try {
//                JSONObject jobj = new JSONObject(jobjStr);
//                BarrageBean bean = new BarrageBean();
//                String name = jobj.getString("name");
//                String danmu = jobj.getString("danmu");
//                bean.setName(name);
//                bean.setText(danmu);
//                EventBus.getDefault().post(bean);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if (MSG_BarrageImg.equals(code)){
//            String jobjStr = map.get("message");
//            try {
//                JSONObject jobj = new JSONObject(jobjStr);
//                BarrageBean bean = new BarrageBean();
//                String name = jobj.getString("name");
//                String img = jobj.optString("img");
//                bean.setName(name);
//                bean.setImg(img);
//                EventBus.getDefault().post(bean);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if (MSG_LiveVoice.equals(code)){
//            String jobjStr = map.get("message");
//            try {
//                JSONObject jobj = new JSONObject(jobjStr);
//                ScannerManager.comID = jobj.getString("userAccid");
//                ScannerManager.trId = jobj.getString("trId");
//                String name = jobj.getString("userName");
//                String hpic = jobj.optString("img");
//                IntentHelper.OpenAnswerActivity(context,name,hpic,true);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if (MSG_AudienceVoice.equals(code)){
//            String jobjStr = map.get("message");
//            try {
//                JSONObject jobj = new JSONObject(jobjStr);
//                ScannerManager.comID = jobj.getString("accid");
//                ScannerManager.mMeetingName = jobj.getString("roomName");
//                IntentHelper.OpenAudienceVoiceActivity(context);
//                EventBus.getDefault().post(new EventBean(Const.Event_Launch));//拨打电话界面销毁
//
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

    }


    //通知消息到达客户端时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：通过参数message从而获得通知消息，具体请看官方SDK文档

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        //打印消息方便测试
        BaseUtil.e("通知消息到达了");
        BaseUtil.e("通知消息是"+message.toString());
//        Map<String,String> map = message.getExtra();
//        Log.e("@#",map.get("bili"));
        Map<String,String> map = message.getExtra();
        String code = map.get("code");
        if (TextUtils.isEmpty(code) && "10".equals(code)){
            //系统消息
            HawkUtil.addPushMessage(message);
            EventBus.getDefault().post(new EventBean(Const.Event_SystemFirst,message));
        }

    }

    //用户手动点击通知栏消息时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：1. 通过参数message从而获得通知消息，具体请看官方SDK文档
    //2. 设置用户点击消息后打开应用 or 网页 or 其他页面

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

        //打印消息方便测试
        BaseUtil.e("用户点击了通知消息");
        BaseUtil.e("通知消息是" + message.toString());
        BaseUtil.e("点击后,会进入应用" );
        Map<String,String> map = message.getExtra();





    }

    //用来接收客户端向服务器发送命令后的响应结果。
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        System.out.println(command );


        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印信息便于测试注册成功与否
                System.out.println("注册成功");
                Log.e("@#","注册成功");

            } else {
                System.out.println("注册失败");
                Log.e("@#","注册失败"+message.toString());
            }
        }
    }

    //用于接收客户端向服务器发送注册命令后的响应结果。
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        System.out.println(command );

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印日志：注册成功
                System.out.println("");
                Log.e("@#","注册命令完毕 reId="+MiPushClient.getRegId(context));

            } else {
                //打印日志：注册失败
                System.out.println("注册失败");
                Log.e("@#","注册命令失败");
            }
        } else {
            System.out.println("其他情况"+message.getReason());
            Log.e("@#","其他情况"+message.getReason());
        }
    }


}
