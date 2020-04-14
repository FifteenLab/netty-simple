package com.popcorn.io.nio.buffer;

import java.nio.IntBuffer;

/**
 * Buffer 基础使用
 *
 * @author Jack
 * @since 2019-08-23
 */
public class BasicBuffer {

    public static void main(String[] args) {
        // 创建一个Buffer
        IntBuffer buffer = IntBuffer.allocate(6);

        for (int i = 0; i < 4; i++) {
            buffer.put(2 * (i + 1));
        }

        buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

        buffer.clear();
        // buffer.flip();
        for (int i = 0; i < 5; i++) {
            buffer.put(3 * (i + 1));
        }
    }
}
