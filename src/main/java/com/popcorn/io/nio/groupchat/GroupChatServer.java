package com.popcorn.io.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 群聊服务
 *
 * @author Jack
 * @since 2019-08-25
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int DEFAULT_PORT = 6000;
    private static final int DEFAULT_SELECT_TIMEOUT = 1000;
    private static final int DEFAULT_BUFFER_SIZE = 127;
    private boolean start = true;

    public GroupChatServer() throws IOException {
        this(DEFAULT_PORT);
    }

    public GroupChatServer(int port) throws IOException {
        selector = Selector.open();

        listenChannel = ServerSocketChannel.open();
        listenChannel.bind(new InetSocketAddress(port));
        listenChannel.configureBlocking(false);
        listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server started.");
    }

    /**
     * 监听
     * @throws IOException
     */
    public void listen() throws IOException {
        while (start) {
            if (selector.select(DEFAULT_SELECT_TIMEOUT) == 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            SelectionKey selectionKey;
            while (iterator.hasNext()) {
                selectionKey = iterator.next();
                // 移除，防止重复操作
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    this.handleAccept(selectionKey);
                } else if (selectionKey.isReadable()) {
                    handleRead(selectionKey);
                }

            }
        }
    }

    /**
     * 处理读取操作
     * @param selectionKey
     * @throws IOException
     */
    private void handleRead(SelectionKey selectionKey) throws IOException {
        String msg = this.readData(selectionKey);
        // 转发消息
        if (msg != null) {
            this.send2Other(msg, selectionKey.channel());
        }
    }

    /**
     * 转发消息给其他客户端
     * @param msg
     * @param self
     */
    private void send2Other(String msg, Channel self) throws IOException {
        for (SelectionKey selectionKey : selector.keys()) {
            Channel channel = selectionKey.channel();

            if (channel instanceof SocketChannel && channel != self) {
                SocketChannel dest = (SocketChannel) channel;

                dest.write(ByteBuffer.wrap(msg.getBytes()));
            }
        }

    }


    /**
     * 接收新连接
     * @param selectionKey
     * @throws IOException
     */
    private void handleAccept(SelectionKey selectionKey) throws IOException {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println(socketChannel.getRemoteAddress() + " 上线");
    }

    /**
     * 读取客户端消息
     * @param selectionKey
     * @return
     */
    private String readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        StringBuilder msg = new StringBuilder();

        try {
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                System.arraycopy(buffer.array(), buffer.position(), bytes, 0, bytes.length);
                msg.append(new String(bytes));
                buffer.clear();
            }
            if (msg.length() == 0) {
                System.out.println(socketChannel.getRemoteAddress() + " 下线");
                selectionKey.cancel();
                socketChannel.close();
            } else {
                System.out.println(socketChannel.getRemoteAddress() + " <<< " + msg.toString());
            }
        } catch (IOException e) {
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return msg.length() == 0 ? null :msg.toString();
    }

    public void close() throws IOException {
        start = false;

        if (selector != null) {
            selector.close();
        }

        if (listenChannel != null) {
            listenChannel.close();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
