package com.example.knowledgepointsharelib.command.sample1;

/**
 * Created by sunjie on 2018/5/7.
 */

public class ConcreteReceiver2 extends Receiver{
    //每个接收者都必须处理一定的业务逻辑
    @Override
    public void doSomething(){
        System.out.println("喂马");
    }
}