package com.popcorn.io.nio.buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer ：可让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次。
 *
 * @author Jack
 * @since 2019-08-24
 */
public class MappedByteBufferSimple {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("./file01.txt", "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte)'H');
        mappedByteBuffer.put(3, (byte)'L');
        randomAccessFile.close();
        System.out.println("修改成功");
    }
}
