package com.example.sunjie.knowledgepointshareproject.builder.sample1;

/**
 * Created by sunjie on 2018/5/5.
 */

public class ChickenBurger extends Burger {

    @Override
    public float price() {
        return 50.5f;
    }

    @Override
    public String name() {
        return "Chicken Burger";
    }
}
