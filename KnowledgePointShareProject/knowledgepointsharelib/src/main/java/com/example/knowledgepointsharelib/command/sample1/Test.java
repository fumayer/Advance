package com.example.knowledgepointsharelib.command.sample1;

/**
 * Created by sunjie on 2018/5/7.
 */

public class Test {
    public static void main(String[] args) {
        Invoker invoker = new Invoker();
        Receiver receiver = new ConcreteReceiver1();

        Command command = new ConCreteCommand1(receiver);
        invoker.setCommand(command);
        invoker.action();
    }
}
