package com.example.knowledgepointsharelib.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/10/13.
 */

public class MyServer {
    //定义保存所有的Socket，与客户端建立连接得到一个Socket
    public static List<Socket> socketList = new ArrayList<Socket>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8888);
        while (true) {
            System.out.println("start listening port 8888.");
            Socket socket = server.accept();
            System.out.println("connect succeed.");
            socketList.add(socket);
            //每当客户端连接之后启动一条ServerThread线程为该客户端服务
            new Thread(new MyServerRunnable(socket)).start();
        }
    }

    public static class MyServerRunnable implements Runnable {
        Socket socket = null; // 定义当前线程处理的Socket
        BufferedReader bufferedReader = null; // 该线程所处理的Socket所对应的输入流

        public MyServerRunnable(Socket socket) throws IOException {
            this.socket = socket;
//            将服务器端的输入流的数据放入读Buffer中
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            String content = null;
            //采用循环不断的从Socket中读取客户端发送过来的数据
            while ((content = readFromClient()) != null) {
                //遍历socketList中的每一个Socket，将读取的内容向每个Socket发送一次
                for (Socket socket : MyServer.socketList) {
                    OutputStream outputStream;
                    try {
                        outputStream = socket.getOutputStream();
                        outputStream.write((content + "\n").getBytes("utf-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 定义读取客户端的信息
        public String readFromClient() {
            try {
                return bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
