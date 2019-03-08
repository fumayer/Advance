package com.example.knowledgepointsharelib.designMode.command.sample3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/5/7.
 */

public class Invoker {

    /**
     * 不要为了添加基于猜测的、实际不需要的功能，如果不清楚一个系统是否需要命令模式，
     * 一般就不要着急去实现它， 事实上，在需要的时候通过重构实现这个模式并不困难，
     * 只有在真正需要撤销/恢复操作等功能时，把原来的代码重构为命令模式才有意义
     */
    private static List<Command> list = new ArrayList<>();


    /**
     * 方便的执行撤销操作
     */
    public void remove(Command command) {
        list.remove(command);
    }

    /**
     * 方便的执行记录操作
     */
    public void record(Command command) {
        list.add(command);
    }

    /**
     * 添加命令
     */
    public void setCommand(Command command) {
        record(command);
    }

    /**
     * 执行开灯命令
     */
    public void execOnLightCommand() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Command command = list.get(i);
                if (command != null && command instanceof LightOnCommand) {
                    command.exec();
                }
            }
        }
    }

    /**
     * 执行关灯命令
     */
    public void execOffLightCommand() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Command command = list.get(i);
                if (command != null && command instanceof LightOffCommand) {
                    command.exec();
                }
            }
        }
    }

}
