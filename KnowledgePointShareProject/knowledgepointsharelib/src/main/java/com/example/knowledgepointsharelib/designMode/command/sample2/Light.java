package com.example.knowledgepointsharelib.designMode.command.sample2;

/**
 * Created by sunjie on 2018/5/7.
 */

public class Light implements HouseholdAppliances{
    @Override
    public void on() {
        System.out.println("the light on");
    }

    @Override
    public void off() {
        System.out.println("the light off");
    }
}
