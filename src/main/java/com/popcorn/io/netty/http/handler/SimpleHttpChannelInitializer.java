package com.popcorn.io.netty.http.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 客户端处理器
 *
 * @author Jack
 * @since 2020-04-01
 */
public class SimpleHttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加Netty 提供的HttpServerCodec
        // HttpServerCodec 基于HTTP的编解码器
        pipeline.addLast("httpCodec", new HttpServerCodec());
        // 添加自定义编解码器
        pipeline.addLast("serverHandler", new SimpleServerHandler());
    }
}
