package com.example.knowledgepointsharelib.command.sample1;

/**
 * Created by sunjie on 2018/5/7.
 */

//具体的Command类
public class ConCreteCommand1 extends Command {
    //对哪个Receiver类进行命令处理
    private Receiver receiver;
    //构造函数传递接收者
    public ConCreteCommand1(Receiver _receiver){
        this.receiver = _receiver;
    }

    //必须实现一个命令
    @Override
    public void execute() {
        //业务处理
        this.receiver.doSomething();
    }
}

