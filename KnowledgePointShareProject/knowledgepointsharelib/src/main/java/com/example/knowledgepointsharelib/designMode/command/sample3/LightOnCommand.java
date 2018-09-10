package com.example.knowledgepointsharelib.designMode.command.sample3;

/**
 * Created by sunjie on 2018/5/7.
 */

public class LightOnCommand  implements Command{
    private Receiver receiver;

    public LightOnCommand(Receiver receiver) {
        this.receiver = receiver;
    }


    @Override
    public void exec() {
        receiver.onLight();
    }
}
