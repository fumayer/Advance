package com.example.knowledgepointsharelib.designMode.command.sample3;

/**
 * Created by sunjie on 2018/5/7.
 */

public class Receiver {
    public void onLight(){
        System.out.println("开灯");
    }

    public void offLight(){
        System.out.println("关灯");
    }
}
