package com.popcorn.io.nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Buffer 分散和聚集
 *
 * @author Jack
 * @since 2019-08-25
 */
public class ScatteringAndGatteringSimple {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(7000));

        // 创建Buffer 数据
        ByteBuffer[] buffers = new ByteBuffer[]{ByteBuffer.allocate(5), ByteBuffer.allocate(3)};

        // 等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        int stepSize = 7;
        while(true) {
            int readSize = 0;
            while (readSize < stepSize) {
                readSize += socketChannel.read(buffers);

                println(buffers);
            }
            // 输出读到的信息
            Arrays.stream(buffers).forEach(Buffer::flip);

            // 回显到客户端
            int writeSize = 0;
            while (writeSize < stepSize) {
                writeSize += socketChannel.write(buffers);
                System.out.println("=====回显=====");
                println(buffers);
            }

            // clear
            Arrays.stream(buffers).forEach(Buffer::clear);
        }
    }

    public static void println(ByteBuffer[] buffers) {
        Arrays.stream(buffers).map(buffer ->
                "pos:" + buffer.position() + ", cap: " + buffer.capacity() + ", lim:" + buffer.limit()
        ).forEach(System.out::println);
    }
}
