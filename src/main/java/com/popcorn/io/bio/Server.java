package com.popcorn.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO 服务端源码
 */
@Slf4j
public class Server {

    private static int DEFAULT_PORT = 9999;

    private static ServerSocket serverSocket;



    public static void main(String[] args) throws IOException {
        // 创建线程池
        Executor executor = Executors.newFixedThreadPool(10);

        ServerSocket serverSocket = new ServerSocket(9999);
        println("服务启动成功");
        while (true) {
            println("等待连接。。。。。");
            // 没有新连接时，阻塞等待
            Socket socket = serverSocket.accept();
            println("接收到新连接");
            executor.execute(new ServerHandler(socket));
        }

    }

    public static void println(String msg) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
    }

}
