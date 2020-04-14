package com.popcorn.io.nio.channel;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用一个Buffer 实现文件拷贝
 *
 * @author Jack
 * @since 2019-08-24
 */
public class FileChannelCopySimple {

    public static void main(String[] args) throws IOException {
        // 创建一个输入流
        FileInputStream fileInputStream = new FileInputStream("./file01.txt");
        // 通过输入流获取对应的文件Channel
        FileChannel fileInputChannel = fileInputStream.getChannel();

        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("./file02.txt");
        FileChannel fileOutputChannel = fileOutputStream.getChannel();

        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);

        while (true) {
            // ** 清空buffer **
            byteBuffer.clear();

            int read = fileInputChannel.read(byteBuffer);
            System.out.println(read);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            fileOutputChannel.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
