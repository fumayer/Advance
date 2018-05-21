package com.example.sunjie.knowledgepointshareproject.builder.sample1.drink;

/**
 * Created by sunjie on 2018/5/5.
 */

public class Pepsi extends ColdDrink {

    @Override
    public float price() {
        return 35.0f;
    }

    @Override
    public String name() {
        return "Pepsi";
    }
}
