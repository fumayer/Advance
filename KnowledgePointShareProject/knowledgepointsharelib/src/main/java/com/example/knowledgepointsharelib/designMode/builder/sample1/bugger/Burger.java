package com.example.knowledgepointsharelib.designMode.builder.sample1.bugger;

import com.example.knowledgepointsharelib.designMode.builder.sample1.Item;
import com.example.knowledgepointsharelib.designMode.builder.sample1.pack.Packing;
import com.example.knowledgepointsharelib.designMode.builder.sample1.pack.Wrapper;

/**
 * Created by sunjie on 2018/5/5.
 * 汉堡（Burger）可以是素食汉堡（Veg Burger）或鸡肉汉堡（Chicken Burger），它们是包在纸盒中Wrapper
 */

public abstract class Burger implements Item {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

}
