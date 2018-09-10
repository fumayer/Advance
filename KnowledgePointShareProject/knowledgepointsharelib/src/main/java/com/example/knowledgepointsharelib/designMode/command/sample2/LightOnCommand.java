package com.example.knowledgepointsharelib.designMode.command.sample2;

/**
 * Created by sunjie on 2018/5/7.
 */

public class LightOnCommand implements Command {
    private Light light;

    LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}