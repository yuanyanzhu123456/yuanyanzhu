package com.geo.rcs.modules.source.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 用于模拟无锡银行返回结果
 * Created by 曾志 on 2019/1/18.
 */

@RestController
@RequestMapping("/wuXidataSource")
public class WuXiTest {


    public static void main(String[] args) {
        System.out.println("服务器启动...\n");
        WuXiTest server = new WuXiTest();
        server.init();
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(3456);
            while (true) {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(client);
            }
        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;
        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        public void run() {
            try {
                // 读取客户端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                // 处理客户端数据
                System.out.println("客户端发过来的内容:" + clientInputStr);
                // 向客户端回复信息
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append("<?xml version='1.0' encoding='UTF-8'?>").append("<hosts><resp><GJJYJE>20181228</GJJYJE><DWJCBL_12>1|1|1|3</DWJCBL_12><GJJYJE>8000</GJJYJE></resp></hosts>");
                // 发送键盘输入的一行
                out.writeUTF(sb.toString());
                out.close();
                input.close();
            } catch (Exception e) {
                System.out.println("服务器 run 异常: " + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        System.out.println("服务端 finally 异常:" + e.getMessage());
                    }
                }
            }
        }
    }
}
