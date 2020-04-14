package com.popcorn.io.nio.channel;



import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用FileChannel 完成文件读操作
 *
 * @author Jack
 * @since 2019-08-24
 */
public class FileChannelReadSimple {

    public static void main(String[] args) throws IOException {
        // 创建一个输入流
        FileInputStream fileInputStream = new FileInputStream("./file01.txt");

        // 通过输入流获取对应的文件Channel
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        // 从Channel 读取数据到 Buffer
        fileChannel.read(byteBuffer);
        byteBuffer.flip();

        // 读取文件内容
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);

        System.out.println(new String(bytes));

        fileInputStream.close();
    }
}
