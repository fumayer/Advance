package com.example.knowledgepointsharelib.designMode.builder.sample1.dirnk;

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
