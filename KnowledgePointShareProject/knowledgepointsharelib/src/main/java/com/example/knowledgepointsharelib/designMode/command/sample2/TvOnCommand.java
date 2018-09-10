package com.example.knowledgepointsharelib.designMode.command.sample2;

/**
 * Created by sunjie on 2018/5/7.
 */

public class TvOnCommand implements Command {
    private Tv tv;

    public TvOnCommand(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.on();
    }

    @Override
    public void undo() {
        tv.off();
    }
}
