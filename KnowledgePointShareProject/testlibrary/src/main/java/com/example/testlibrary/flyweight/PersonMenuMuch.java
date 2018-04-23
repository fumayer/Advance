package com.example.testlibrary.flyweight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunjie on 2018/4/22.
 */

public class PersonMenuMuch implements Menu {
    private Map MenuList = new HashMap();

    public PersonMenuMuch() {
    }

    //增加一个新的单纯享元对象
    public void add(String key, Menu menu) {
        MenuList.put(key, menu);
    }

    //两个无为的方法
    @Override
    public synchronized void setPersonMenu(String person, List list) {
    }

    @Override
    public List findPersonMenu(String person, List list) {
        List nothing = null;
        return nothing;
    }
}
