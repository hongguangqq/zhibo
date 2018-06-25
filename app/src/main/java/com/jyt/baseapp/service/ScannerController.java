package com.jyt.baseapp.service;

import android.content.Context;
import android.content.Intent;

import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;

/**
 * @author LinWei on 2018/6/25 10:40
 */
public class ScannerController {

    private ScannerController() {
    }

    public static ScannerController getInstance() {
        return LittleMonkProviderHolder.sInstance;
    }

    // 静态内部类
    private static class LittleMonkProviderHolder {
        private static final ScannerController sInstance = new ScannerController();
    }

    private ScannerCallBack mScannerCallBack;

    /**
     * 开启服务悬浮窗
     */
    public void startMonkServer(Context context ) {
        Intent intent = new Intent(context, ScannerService.class);
        context.startService(intent);
    }

    /**
     * 关闭悬浮窗
     */
    public void stopMonkServer(Context context) {
        Intent intent = new Intent(context, ScannerService.class);
        context.stopService(intent);
    }

    /**
     * 注册监听
     */
    public void registerCallLittleMonk(ScannerCallBack callLittleMonk) {
        mScannerCallBack = callLittleMonk;
    }

    public void createAndJoinRoom(){
        if (mScannerCallBack == null) return;
        mScannerCallBack.createAndjoinRoom();
    }

    public void joinRoom(String roomId){
        if (mScannerCallBack == null) return;
        mScannerCallBack.joinRoom(roomId);
    }

    public void show(){
        if (mScannerCallBack == null) return;
        mScannerCallBack.show();
    }

    public AVChatSurfaceViewRenderer getLocalRender(){
        if (mScannerCallBack == null) return null;
        return mScannerCallBack.getLocalRender();
    }

    public AVChatSurfaceViewRenderer getBypassRender(){
        if (mScannerCallBack == null) return null;
        return mScannerCallBack.getBypassRender();
    }

    public void closeScanner(boolean isReleaseRtc, boolean isLeaveRoom ){
        if (mScannerCallBack == null) return;
        mScannerCallBack.closeScanner(isReleaseRtc,isLeaveRoom);
    }

    public void closeConnection(){
        if (mScannerCallBack == null) return;
        mScannerCallBack.closeConnection();
    }
}
