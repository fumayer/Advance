package com.example.knowledgepointsharelib.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by sunjie on 2018/10/13.
 */

public class Easy {
    public static void main(String[] args) throws IOException {
//           创建Socket连接
//        创建Socket对象 & 指定服务端的IP及端口号
//        判断客户端和服务器是否连接成功
        Socket socket = null;
        socket = new Socket("192.168.1.32", 1989);
        socket.isConnected();


//           接收服务器的数据
//        创建输入流对象InputStream,创建输入流读取器对象 并传入输入流对象
//        通过输入流读取器对象 接收服务器发送过来的数据
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        br.readLine();

//        发送数据到服务器, 从Socket获得输出流对象OutputStream, 写入需要发送的数据到输出流对象中
        OutputStream os = socket.getOutputStream();
        os.write("Carson_Ho".getBytes("utf-8"));
        os.flush();


//        断开客户端 & 服务器 连接
//        关闭输入流、输出流、最终关闭整个Socket连接
        os.close();
        br.close();
        socket.close();
    }


}
