package com.popcorn.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 类型化放入数据
 *
 * @author Jack
 * @since 2019-08-24
 */
public class ByteBufferPutGet {

    public static void main(String[] args) {
        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(512);

        // 类型化
        buffer.putChar('A');
        buffer.putInt(1);
        buffer.putShort((short)1);
        buffer.putLong(10L);

        buffer.flip();

        System.out.println(buffer.getChar());
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getShort());
        // 超出buffer限制  throw java.nio.BufferUnderflowException
//        System.out.println(buffer.getLong());
    }
}
