package com.example.sunjie.knowledgepointshareproject.abstract_factory2;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/2.
 */

public class Lion implements Carnivore {
    @Override
    public void Eat(Herbivore h) {
        LogUtil.e("Lion","10-----Eat--->"+getName() + " eats " + h.getName() );
    }

    @Override
    public String getName() {
        return "狮子";
    }
}
