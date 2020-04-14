package com.popcorn.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 只读
 *
 * @author Jack
 * @since 2019-08-24
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {
        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(512);

        // 类型化
        buffer.putChar('A');
        buffer.putInt(1);
        buffer.putShort((short)1);
        buffer.putLong(10L);

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        readOnlyBuffer.flip();

        // System.out.println(readOnlyBuffer.getClass()); //class java.nio.HeapByteBufferR
        System.out.println(readOnlyBuffer.getChar());
//         put throw java.nio.ReadOnlyBufferException
//        readOnlyBuffer.putShort((short)1);
    }
}
