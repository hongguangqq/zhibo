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
    private static final String MSG_RingBack = "7";//主播回拨
    private static final String MSG_AudienceHangUp = "8";//观众收到主播的回拨，选择挂断


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
                Log.e("@#","ASD"+"onSuccess-requsetGreateRoom");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError-requsetGreateRoom "+ errorCode.getMessage());
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
                Log.e("@#","ASD"+"onSuccess-liveAgreeCreatRoom");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError-liveAgreeCreatRoom "+ errorCode.getMessage());
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
                Log.e("@#","ASD"+"onSuccess-audienceHangUp");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError-audienceHangUp "+ errorCode.getMessage());
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
                Log.e("@#","ASD"+"onSuccess-liveRejectCall");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError-liveRejectCall "+ errorCode.getMessage());
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
                Log.e("@#","ASD"+"onSuccess-requsetGreateVoiceRoom");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError-requsetGreateVoiceRoom "+ errorCode.getMessage());
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
                Log.e("@#","ASD"+"onSuccess-liveAgreeCreatVoiceRoom");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError-liveAgreeCreatVoiceRoom "+ errorCode.getMessage());
            }
        });
    }

    /**
     * 主播回拨观众
     * @param roomName
     * @param uid
     */
    public static void LiveRingBack(String roomName , String uid){
        NMRCCallMessage msg = new NMRCCallMessage(MSG_RingBack , roomName , Const.getUserNick() , Const.getUserHeadImg() , Const.getUserID() ,Const.getWyAccount(), ScannerManager.trId);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, uid, msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Log.e("@#","ASD"+"onSuccess-LiveRingBack");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Log.e("@#","ASD"+"onError-LiveRingBack "+ errorCode.getMessage());
            }
        });
    }




}
