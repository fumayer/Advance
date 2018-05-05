package com.example.knowledgepointsharelib.builder.sample1.bugger;

/**
 * Created by sunjie on 2018/5/5.
 */

public class VegBurger extends Burger {

    @Override
    public float price() {
        return 25.0f;
    }

    @Override
    public String name() {
        return "Veg Burger";
    }
}
