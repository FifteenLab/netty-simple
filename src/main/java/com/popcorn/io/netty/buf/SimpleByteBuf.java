package com.popcorn.io.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SimpleByteBuf {

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(10);

        int i = 0;
        while (byteBuf.isWritable()) {
            byteBuf.writeByte(i++);
        }

        while (byteBuf.isReadable()) {
            System.out.println(byteBuf.readByte());
        }

    }
}
