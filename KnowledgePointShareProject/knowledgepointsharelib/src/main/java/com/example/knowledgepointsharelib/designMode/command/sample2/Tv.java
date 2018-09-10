package com.example.knowledgepointsharelib.designMode.command.sample2;

/**
 * Created by sunjie on 2018/5/7.
 */

public class Tv implements HouseholdAppliances {
    @Override
    public void on() {
        System.out.println("the TV on");
    }

    @Override
    public void off() {
        System.out.println("the TV off");
    }
}