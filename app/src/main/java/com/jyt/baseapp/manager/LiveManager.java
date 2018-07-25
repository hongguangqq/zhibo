package com.jyt.baseapp.manager;

import android.util.Log;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.service.ScannerManager;
import com.jyt.baseapp.view.widget.NMRCCallMessage;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * @author LinWei on 2018/7/25 11:38
 */
public class LiveManager {
    private static final String MSG_Live = "1";//主播收到视频创建房间请求
    private static final String MSG_Audience = "2";//观众收到邀请加入房间-视频
    private static final String MSG_HangUp = "3";//用户挂断-视频
    private static final String MSG_LiveHangUp = "4";//主播挂断-视频
    private static final String MSG_LiveVoice = "5";//主播收到音频创建房间请求
    private static final String MSG_AudienceVoice = "6";//观众收到邀请加入房间-音频


    /**
     * 观众向主播发起视频开房请求
     * @param uid 请求对象的用户ID
     */
    public static void  requsetGreateRoom(String uid){
        NMRCCallMessage msg = new NMRCCallMessage(MSG_Live , null , Const.getUserNick() , Const.getUserHeadImg() , Const.getUserID() ,Const.getWyAccount(), ScannerManager.trId);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("@#","ASD"+"onSuccess");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError "+ errorCode.getMessage());
            }
        });
    }

    /**
     * 主播同意开房，邀请用户加入-视频
     *
     */
    public static void liveAgreeCreatRoom(String roomName , String uid){
        NMRCCallMessage msg = new NMRCCallMessage(MSG_Audience , roomName , Const.getUserNick() , Const.getUserHeadImg() , Const.getUserID() ,Const.getWyAccount(), ScannerManager.trId);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("@#","ASD"+"onSuccess");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError "+ errorCode.getMessage());
            }
        });
    }

    /**
     * 当主播未接听前，观众挂断
     * @param uid
     */
    public static void audienceHangUp(String uid){
        NMRCCallMessage msg = new NMRCCallMessage(MSG_HangUp , null , Const.getUserNick() , Const.getUserHeadImg() , Const.getUserID() ,Const.getWyAccount(), ScannerManager.trId);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("@#","ASD"+"onSuccess");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError "+ errorCode.getMessage());
            }
        });
    }

    /**
     * 主播拒听
     * @param uid
     */
    public static void liveRejectCall(String uid){
        NMRCCallMessage msg = new NMRCCallMessage(MSG_LiveHangUp , null , Const.getUserNick() , Const.getUserHeadImg() , Const.getUserID() ,Const.getWyAccount(), ScannerManager.trId);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("@#","ASD"+"onSuccess");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError "+ errorCode.getMessage());
            }
        });
    }

    /**
     * 观众向主播发起音频开房请求
     * @param uid 请求对象的用户ID
     */
    public static void  requsetGreateVoiceRoom(String uid){
        NMRCCallMessage msg = new NMRCCallMessage(MSG_LiveVoice , null , Const.getUserNick() , Const.getUserHeadImg() , Const.getUserID() ,Const.getWyAccount(), ScannerManager.trId);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("@#","ASD"+"onSuccess");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError "+ errorCode.getMessage());
            }
        });
    }

    /**
     * 主播同意开房，邀请用户加入-视频
     *
     */
    public static void liveAgreeCreatVoiceRoom(String roomName , String uid){
        NMRCCallMessage msg = new NMRCCallMessage(MSG_AudienceVoice , roomName , Const.getUserNick() , Const.getUserHeadImg() , Const.getUserID() ,Const.getWyAccount(), ScannerManager.trId);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("@#","ASD"+"onSuccess");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError "+ errorCode.getMessage());
            }
        });
    }
}
