package com.example.knowledgepointsharelib.designMode.command.sample1;

/**
 * Created by sunjie on 2018/5/7.
 */

//调用者Invoker类
public class Invoker {
    private Command command;

    public void setCommand(Command _command){
        this.command = _command;
    }

    public void action() {
        this.command.execute();
    }
}