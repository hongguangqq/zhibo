package com.jyt.baseapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

/**
 * @author LinWei on 2018/6/25 10:30
 * #####################################################
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无bug             #
 * #                                                   #
 * #####################################################
 */
public class ScannerService extends Service implements ScannerCallBack,AVChatStateObserverLite {
    private boolean isMaster;//是否为主播

    @Override
    public void onCreate() {
        super.onCreate();
        ScannerController.getInstance().registerCallLittleMonk(this);
        //初始化悬浮窗UI
        initWindowData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 初始化直播小窗口
     */
    private void initWindowData() {
        ScannerManager.init(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ScannerManager.removeFloatWindowManager();
        AVChatManager.getInstance().observeAVChatState(this,false);
    }

    /************************* ScannerCallBack *****************************/

    @Override
    public void createAndjoinRoom() {
        ScannerManager.createAndJoinRoom(this);
    }

    @Override
    public void joinRoom(String roomId) {
        ScannerManager.joinRoom(this,roomId);
    }

    @Override
    public AVChatSurfaceViewRenderer getLocalRender() {
        return ScannerManager.getmLocalRender();
    }

    @Override
    public AVChatSurfaceViewRenderer getBypassRender() {
        return ScannerManager.getmBypassRender();
    }

    @Override
    public void show() {
        ScannerManager.show();
    }

    @Override
    public void closeScanner(boolean isReleaseRtc, boolean isLeaveRoom) {
        releaseRtc(isReleaseRtc, isLeaveRoom);
    }

    @Override
    public void closeConnection() {
        AVChatManager.getInstance().observeAVChatState(this, false);
    }


    /************************* AVChatStateObserverLite *****************************/
    @Override
    public void onJoinedChannel(int i, String s, String s1, int i1) {

    }

    @Override
    public void onUserJoined(String s) {

    }

    @Override
    public void onUserLeave(String s, int i) {

    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onProtocolIncompatible(int i) {

    }

    @Override
    public void onDisconnectServer(int i) {
        releaseRtc(true, true);
    }

    @Override
    public void onNetworkQuality(String s, int i, AVChatNetworkStats avChatNetworkStats) {

    }

    @Override
    public void onCallEstablished() {

    }

    @Override
    public void onDeviceEvent(int i, String s) {

    }

    @Override
    public void onConnectionTypeChanged(int i) {

    }

    @Override
    public void onFirstVideoFrameAvailable(String s) {

    }

    @Override
    public void onFirstVideoFrameRendered(String s) {

    }

    @Override
    public void onVideoFrameResolutionChanged(String s, int i, int i1, int i2) {

    }

    @Override
    public void onVideoFpsReported(String s, int i) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame avChatVideoFrame, boolean b) {
        return true;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame avChatAudioFrame) {
        return true;
    }

    @Override
    public void onAudioDeviceChanged(int i) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> map, int i) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats avChatSessionStats) {

    }

    @Override
    public void onLiveEvent(int i) {

    }

    private void releaseRtc(boolean isReleaseRtc, boolean isLeaveRoom){
//        if (mVideoEffect != null) {
//            mVideoEffectHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("@#", "releaseRtc unInit");
//                    mVideoEffect.unInit();
//                    mVideoEffect = null;
//                }
//            });
//        }
        if (isReleaseRtc) {
            AVChatManager.getInstance().setupLocalVideoRender(null, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            AVChatManager.getInstance().stopVideoPreview();
            AVChatManager.getInstance().disableVideo();

        }
        if (isLeaveRoom){
            AVChatManager.getInstance().leaveRoom2(ScannerManager.mMeetingName, new AVChatCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("@#","leave channel success");
                    ScannerManager.mMeetingName = "";
                }

                @Override
                public void onFailed(int i) {
                    Log.e("@#","leave channel failed, code:" + i);
                }

                @Override
                public void onException(Throwable throwable) {
                    Log.e("@#","leave channel exception, throwable:" + throwable.getMessage());
                }
            });
        }
        AVChatManager.getInstance().disableRtc();
    }
}
