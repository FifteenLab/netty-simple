package com.popcorn.io.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty 服务端
 *
 * @author Jack
 * @since 2020-03-31
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup 和WorkerGroup
        // BossGroup 只处理连接请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // WorkerGroup 和客户端通讯并进行业务处理
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建数据库启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {// 创建一个通道注册对象
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 每个客户端连接建立后，创建SocketChannel
                System.out.println("new client. ch=" + ch);
                ch.pipeline().addLast(new NettyServerHandler());
            }
        });
        // 设置线程组
        // 使用NioServerSocketChannel 作为服务器通道实现
        // 设置线程队列得到的连接个数
        // 设置保持活动连接状态
        System.out.println("server started.");
        ChannelFuture future = serverBootstrap.bind(7000).sync();

        // 注册监听器，监听关心的事件
        future.addListener((ChannelFutureListener) listener -> {
            if (listener.isSuccess()) {
                System.out.println("[" + Thread.currentThread().getName() + "] listener 7000 success.");
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] listener failure." );
            }
         });
        // 监听关闭事件
        future.channel().closeFuture().sync();
    }
}
