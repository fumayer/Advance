package com.example.sunjie.knowledgepointshareproject.builder.sample1.drink;

import com.example.sunjie.knowledgepointshareproject.builder.sample1.Item;
import com.example.sunjie.knowledgepointshareproject.builder.sample1.pack.Bottle;
import com.example.sunjie.knowledgepointshareproject.builder.sample1.pack.Packing;

/**
 * Created by sunjie on 2018/5/5.
 */

public abstract class ColdDrink implements Item {

    @Override
    public Packing packing() {
        return new Bottle();
    }

    @Override
    public abstract float price();
}
