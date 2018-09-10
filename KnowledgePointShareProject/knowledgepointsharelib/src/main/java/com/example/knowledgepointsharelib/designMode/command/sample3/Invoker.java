package com.example.knowledgepointsharelib.designMode.command.sample3;

/**
 * Created by sunjie on 2018/5/7.
 */

public class Invoker {
    public void invoke(Command command) {
        command.exec();
    }
}
