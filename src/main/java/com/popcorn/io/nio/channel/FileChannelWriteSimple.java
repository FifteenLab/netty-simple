package com.popcorn.io.nio.channel;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用FileChannel 完成文件写操作
 *
 * @author Jack
 * @since 2019-08-24
 */
public class FileChannelWriteSimple {

    public static void main(String[] args) throws IOException {
        String str = "hello world";

        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("./file01.txt");

        // 通过输出流获取对应的文件Channel
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        // 将str 放入 buffer
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();

        // 把Buffer 写入Channel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
