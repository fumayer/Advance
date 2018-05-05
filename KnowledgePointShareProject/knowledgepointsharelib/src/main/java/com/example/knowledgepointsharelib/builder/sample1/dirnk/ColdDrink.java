package com.example.knowledgepointsharelib.builder.sample1.dirnk;

import com.example.knowledgepointsharelib.builder.sample1.pack.Bottle;
import com.example.knowledgepointsharelib.builder.sample1.Item;
import com.example.knowledgepointsharelib.builder.sample1.pack.Packing;

/**
 * Created by sunjie on 2018/5/5.
 * 冷饮（Cold drink）可以是可口可乐（coke）或百事可乐（pepsi），它们是装在瓶子中。Bottle
 */

public abstract class ColdDrink implements Item {

    @Override
    public Packing packing() {
        return new Bottle();
    }

}
