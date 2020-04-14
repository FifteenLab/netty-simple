package com.popcorn.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 使用Channel的transferFrom、transferTo 实现文件拷贝
 *
 * @author Jack
 * @since 2019-08-24
 */
public class FileChannelCopySimpleForTransfer {

    public static void main(String[] args) throws IOException {
        // 创建一个输入流
        FileInputStream fileInputStream = new FileInputStream("./01.jpg");
        // 通过输入流获取对应的文件Channel
        FileChannel sourceChannel = fileInputStream.getChannel();

        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("./02.jpg");
        FileChannel destChannel = fileOutputStream.getChannel();

        // 完成文件拷贝
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
//        sourceChannel.transferTo(0, sourceChannel.size(), destChannel);

        fileInputStream.close();
        fileOutputStream.close();
    }
}
