package com.popcorn.io.netty.simple;

import io.netty.util.NettyRuntime;

public class SimpleApplication {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());
    }
}
