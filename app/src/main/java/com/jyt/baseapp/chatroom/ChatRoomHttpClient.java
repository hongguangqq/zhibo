package com.jyt.baseapp.chatroom;

/**
 * @author LinWei on 2018/6/15 17:18
 */
public class ChatRoomHttpClient {
    private static ChatRoomHttpClient instance;

    public static synchronized ChatRoomHttpClient getInstance() {
        if (instance == null) {
            instance = new ChatRoomHttpClient();
        }

        return instance;
    }

    private ChatRoomHttpClient() {

    }

}
