package com.example.testlibrary.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sunjie on 2018/4/22.
 */

public class FlyWeightFactory {
    private Map menuList = new HashMap();

    private static FlyWeightFactory factory = new FlyWeightFactory();


    private FlyWeightFactory() {
    }

    public static FlyWeightFactory getInstance() {
        return factory;
    }

/*这就是享元模式同工厂模式的不同所在！！
判断如果内蕴状态已经存在就不再重新生成，而是使用原来的，否则就重新生成 */

    public synchronized Menu factory(String dish) {


        if (menuList.containsKey(dish)) {

            return (Menu) menuList.get(dish);

        } else {

            Menu menu = new PersonMenu(dish);

            menuList.put(dish, menu);

            return menu;

        }

    }

//来验证下是不是真的少产生了对象

    public int getNumber() {
        return menuList.size();
    }


    public Menu factory(String[] dish) {
        PersonMenuMuch menu = new PersonMenuMuch();
        String key = null;
        for (int i = 0; i < dish.length; i++) {
            key = dish[i];
            menu.add(key, this.factory(key));//调用了单纯享元角色的工厂方法
        }
        return menu;
    }
}




