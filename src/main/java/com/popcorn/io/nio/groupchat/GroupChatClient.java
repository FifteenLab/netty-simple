package com.popcorn.io.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 群聊客户端
 *
 * @author Jack
 * @since 2019-08-25
 */
public class GroupChatClient {

    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient(String host, int port) throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress(host, port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("client started.");
    }

    /**
     * 发送消息
     * @param msg
     * @throws IOException
     */
    private void send(String msg) throws IOException {
        String body = username + ": " + msg;
        socketChannel.write(ByteBuffer.wrap(body.getBytes()));
    }

    /**
     * 读取消息
     * @throws IOException
     */
    private void read() throws IOException {
        if (selector.select(2000) > 0) {

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            SelectionKey selectionKey;

            while (iterator.hasNext()) {
                selectionKey = iterator.next();

                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    StringBuilder body = new StringBuilder();
                    while (socketChannel.read(buffer) > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        System.arraycopy(buffer.array(), buffer.position(), bytes, 0, bytes.length);
                        body.append(new String(bytes));
                        buffer.clear();
                    }
                    if (body.length() > 0) {
                        System.out.println(body.toString());
                    } else {
                        System.out.println("离线");
                        selectionKey.cancel();
                        socketChannel.close();
                    }
                }
                iterator.remove();
            }

        }
    }

    public void start() throws IOException {
        new Thread(() -> {
            while (true) {
                try {
                    this.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            this.send(scanner.nextLine());
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient groupChatClient = new GroupChatClient("127.0.0.1", 6000);
        groupChatClient.start();
    }
}
