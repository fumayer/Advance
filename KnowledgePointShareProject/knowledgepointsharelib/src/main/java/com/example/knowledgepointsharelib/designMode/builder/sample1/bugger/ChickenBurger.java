package com.example.knowledgepointsharelib.designMode.builder.sample1.bugger;

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
