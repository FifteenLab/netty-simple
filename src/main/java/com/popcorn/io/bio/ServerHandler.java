package com.popcorn.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Server.println("处理连接");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String req;
            while (true) {
                Server.println("等待读取。。。");
                // 当前线程暂时没有数据可读，则线程就阻塞
                if ((req = in.readLine()) == null) {
                    break;
                }

                Server.println(req);

                if (req.equals("bye")) {
                    out.println("bye");
                    break;
                }

                out.println("success");

            }
        } catch (IOException e) {
            e.printStackTrace();
            Server.println(e.getMessage());
        }

    }
}
