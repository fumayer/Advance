package com.example.knowledgepointsharelib.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by sunjie on 2018/10/13.
 */

public class Clone {
    static class Person implements Cloneable {
        String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    String name = "原始";


    public static void main(String[] args) throws CloneNotSupportedException {
        Person aVolatile = new Person("张三");
        System.out.println(aVolatile.name);
        Person clone = (Person) aVolatile.clone();
        clone.name = "李四";
        System.out.println(aVolatile.name);
        TreeMap<String, String> stringStringTreeMap = new TreeMap<>();
        stringStringTreeMap.put("", null);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put(null, null);
        Hashtable<String, String> stringStringHashtable = new Hashtable<>();
        stringStringHashtable.put("", null);

        TreeSet<String> stringStringTreeSet = new TreeSet<>();
        stringStringTreeSet.add(null);

    }


}
