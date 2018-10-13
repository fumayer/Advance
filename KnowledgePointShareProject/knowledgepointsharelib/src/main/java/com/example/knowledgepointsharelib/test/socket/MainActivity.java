package com.example.knowledgepointsharelib.test.socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.knowledgepointsharelib.R;

/**
 * Created by sunjie on 2018/10/13.
 */

public class MainActivity extends Activity implements View.OnClickListener {
    Handler handler;
    // 定义与服务器通信的子线程
    ClientThread clientThread;
    TextView show;
    Button send;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        send.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 如果消息来自子线程
                if (msg.what == 0x123) {
                    // 将读取的内容追加显示在文本框中
                    show.append("\n" + msg.obj.toString());
                    show.setTextSize(50);
                }
            }
        };
        clientThread = new ClientThread(handler);
        // 客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start();
    }

    @Override
    public void onClick(View v) {
        // 当用户按下按钮之后，将用户输入的数据封装成Message
        // 然后发送给子线程Handler
        Message msg = new Message();
        msg.what = 0x345;
        msg.obj = "Android 网络编程--Socket通信编程";
        clientThread.revHandler.sendMessage(msg);
    }
}
