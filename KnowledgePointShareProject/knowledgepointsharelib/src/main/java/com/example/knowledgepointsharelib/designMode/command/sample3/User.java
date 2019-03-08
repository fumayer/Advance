package com.example.knowledgepointsharelib.designMode.command.sample3;

/**
 * Created by sunjie on 2018/5/7.
 */

public class User {
    public static void main(String[] args) {
//        创建执行者Receiver
        Receiver receiver = new Receiver();
//        为执行者的操作创建对应的命令
        Command lightOffCommand = new LightOffCommand(receiver);
        Command lightOnCommand = new LightOnCommand(receiver);
//        创建请求者，并将命令进去
        Invoker invoker = new Invoker();
        invoker.setCommand(lightOffCommand);
        invoker.setCommand(lightOnCommand);

//        这里才是具体用户的操作，
//        获取请求者对象，让请求者执行命令
        invoker.execOnLightCommand();//执行开灯操作
        invoker.execOffLightCommand();//执行关灯操作

    }
}
