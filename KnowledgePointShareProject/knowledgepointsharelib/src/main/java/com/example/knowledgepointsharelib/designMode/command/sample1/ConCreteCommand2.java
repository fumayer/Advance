package com.example.knowledgepointsharelib.designMode.command.sample1;

/**
 * Created by sunjie on 2018/5/7.
 */


public class ConCreteCommand2 extends Command {
    //哪个Receiver类进行命令处理
    private Receiver receiver;
    //构造函数传递接收者
    public ConCreteCommand2(Receiver _receiver){
        this.receiver = _receiver;
    }
    //必须实现一个命令
    @Override
    public void execute() {
        //业务处理
        this.receiver.doSomething();
    }
}