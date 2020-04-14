package com.popcorn.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO Server
 *
 * @author Jack
 * @since 2019-08-25
 */
public class BasicServer {

    public static void main(String[] args) throws IOException {

        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6000));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);

        // 创建一个Selector
        Selector selector = Selector.open();
        // 把serverSocketChannel 注册到 selector，关注事件OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("启动成功 SelectionKey size: " + selector.keys().size());
        while (true) {
            // 等待事件发生
            if (selector.select(1000) == 0 ){
                // 超过等待时间，无事件发生
                continue;
            }

            // 有事件发生，获取所有事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                // 从集合中移除当前SelectionKey，防止重复操作
                keyIterator.remove();
                if (selectionKey.isAcceptable()) {// 对应OP_ACCEPT，有新的客户端连接
                    // 针对新的客户端连接生成一个SocketChannel
                    // accept 阻塞，但是已经有新的客户端连接，所以会直接返回
                    SocketChannel socketChannel = ((ServerSocketChannel)selectionKey.channel()).accept();
                    socketChannel.configureBlocking(false);
                    // 将客户端对应的SocketChannel 注册到selector，关注事件OP_READ 。同时给SocketChannel 关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    System.out.println("SelectionKey size: " + selector.keys().size());

                } else if (selectionKey.isReadable()) {// 对应OP_READ，读取的资源已经准备好
                    System.out.println("===== readable =====");
                    // 获取到SocketChannel
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                     // 获取到关联的Buffer
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // 客户端消息读取到Buffer 中
                    if (socketChannel.read(buffer) > 0) {
                        buffer.flip();
                        String body = new String(buffer.array());
                        System.out.println("<<< " + body);
                        socketChannel.register(selector, SelectionKey.OP_WRITE, body);
                        System.out.println("SelectionKey size: " + selector.keys().size());
                    } else {
                        selectionKey.cancel();
                        socketChannel.close();
                    }
                } else if (selectionKey.isWritable()) {
                    // 获取到SocketChannel
                    System.out.println("===== writeable =====");
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    System.out.println(selectionKey.attachment());
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }


            }
        }

    }
}
