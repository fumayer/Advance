package com.example.knowledgepointsharelib.designMode.command.sample3;

/**
 * Created by sunjie on 2018/5/7.
 */

public class LightOffCommand implements Command {
    private Receiver receiver;

    public LightOffCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exec() {
        receiver.offLight();
    }
}
