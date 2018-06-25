package com.jyt.baseapp.service;

import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;

/**
 * @author LinWei on 2018/6/25 10:36
 */
public interface ScannerCallBack {
    void createAndjoinRoom();

    void joinRoom(String roomId);

    AVChatSurfaceViewRenderer getLocalRender();

    AVChatSurfaceViewRenderer getBypassRender();

    void show();

    void closeScanner(boolean isReleaseRtc, boolean isLeaveRoom);

    void closeConnection();
}
