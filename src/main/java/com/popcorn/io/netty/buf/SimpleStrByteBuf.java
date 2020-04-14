package com.popcorn.io.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class SimpleStrByteBuf {

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world!".getBytes());

        System.out.println("readableBytes: " + byteBuf.readableBytes());
        System.out.println(byteBuf.getCharSequence(0, 4, CharsetUtil.UTF_8));
        System.out.println(byteBuf.getCharSequence(4, 6, CharsetUtil.UTF_8));

        while (byteBuf.isReadable()) {
            System.out.println((char)byteBuf.readByte());
        }
    }
}
