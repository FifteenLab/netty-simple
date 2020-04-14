package com.popcorn.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Netty 服务端处理器
 *
 * @author Jack
 * @since 2020-03-31
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx=" + ctx);

        // 用户自定义的普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("Hello, client~ 01".getBytes(CharsetUtil.UTF_8)));
            System.out.println("[" + Thread.currentThread().getName() + "] send success.");
        });

        // 用户自定义的定时任务
        ctx.channel().eventLoop().schedule(() -> {
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("Hello, client~ schedule".getBytes(CharsetUtil.UTF_8)));
            System.out.println("[" + Thread.currentThread().getName() + "] schedule send success.");
        }, 5, TimeUnit.SECONDS);
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("[" + Thread.currentThread().getName() + "] 客户端发送的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("[" + Thread.currentThread().getName() + "] 客户端地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("Hello, client~".getBytes(CharsetUtil.UTF_8)));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
