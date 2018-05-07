package com.example.knowledgepointsharelib.command.sample3;

/**
 * Created by sunjie on 2018/5/7.
 */

public class Test {
    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        Command lightOffCommand = new LightOffCommand(receiver);
        Command lightOnCommand = new LightOnCommand(receiver);
        Invoker invoker = new Invoker();
        invoker.invoke(lightOffCommand);
        invoker.invoke(lightOnCommand);

    }
}
