package com.example.testlibrary.flyweight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sunjie on 2018/4/22.
 */

public class Client {

    private static FlyWeightFactory factory;

    public static void main(String[] args) {

        List list1 = new ArrayList();

        factory = FlyWeightFactory.getInstance();

        Menu menu = factory.factory("尖椒土豆丝");

        menu.setPersonMenu("ai92", list1);

        menu = factory.factory("红烧肉");

        menu.setPersonMenu("ai92", list1);

        menu = factory.factory("地三鲜");

        menu.setPersonMenu("ai92", list1);

        menu = factory.factory("地三鲜");

        menu.setPersonMenu("ai92", list1);

        menu = factory.factory("红焖鲤鱼");

        menu.setPersonMenu("ai92", list1);

        menu = factory.factory("红烧肉");

        menu.setPersonMenu("ai921", list1);

        menu = factory.factory("红焖鲤鱼");

        menu.setPersonMenu("ai921", list1);

        menu = factory.factory("地三鲜");

        menu.setPersonMenu("ai921", list1);

        System.out.println(factory.getNumber());


        List list2 = menu.findPersonMenu("ai921", list1);

        Iterator it = list2.iterator();

        while (it.hasNext()) {

            System.out.println(" " + it.next());

        }

    }
}
