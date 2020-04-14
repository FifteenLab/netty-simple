package com.popcorn.io.nio.groupchat;

import java.io.IOException;

public class ClientApplication {

    public static void main(String[] args) throws IOException {
        GroupChatClient groupChatClient = new GroupChatClient("127.0.0.1", 6000);
        groupChatClient.start();
    }
}
