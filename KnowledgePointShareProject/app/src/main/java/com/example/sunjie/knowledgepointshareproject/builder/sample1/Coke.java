package com.example.sunjie.knowledgepointshareproject.builder.sample1;

/**
 * Created by sunjie on 2018/5/5.
 */

public class Coke extends ColdDrink {

    @Override
    public float price() {
        return 30.0f;
    }

    @Override
    public String name() {
        return "Coke";
    }
}
