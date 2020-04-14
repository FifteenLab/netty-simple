package com.popcorn.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO Client
 *
 * @author Jack
 * @since 2019-08-25
 */
public class BasicClient {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6000);

        if (!socketChannel.connect(inetSocketAddress)) {
            System.out.print("connect");
            while (!socketChannel.finishConnect()) {
                System.out.print(".");
            }
        }
        // 连接成功
        String body = "hello world";
        ByteBuffer byteBuffer = ByteBuffer.wrap(body.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
