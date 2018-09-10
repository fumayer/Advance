package com.example.sj.lib;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by sunjie on 2018/5/31.
 */

public class Test {
    public static void main(String[] args) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>( );
        linkedHashMap.put("0", "");
        linkedHashMap.put("1", "");
        linkedHashMap.put("2", "");
        linkedHashMap.put("3", "");
        linkedHashMap.put("4", "");
        linkedHashMap.get("2");
        for (String s : linkedHashMap.keySet()) {
            System.out.println(s);
        }
    }
    public static void main7(String[] args) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(16,1,true);
        linkedHashMap.put("0", "");
        linkedHashMap.put("1", "");
        linkedHashMap.put("2", "");
        linkedHashMap.put("3", "");
        linkedHashMap.put("4", "");

        linkedHashMap.get("2");
        for (String s : linkedHashMap.keySet()) {
            System.out.println(s);
        }
    }

    public static void main6(String[] args) {
        String[] strings = new String[]{"a", "b", "c", "d"};
        String[] temS = strings;
        temS[1] = "ÕÅÈý";
        System.out.println(Arrays.toString(strings));
        System.out.println(Arrays.toString(temS));

    }

    public static void main5(String[] args) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

    }

    public static void main4(String[] args) {
        System.out.println(5 & 6);
        System.out.println(1 & 2);
        System.out.println(2 & 3);
        System.out.println(2 & 4);
    }

    public static void main3(String[] args) {
        String[] strings = new String[5];
        strings[4] = "";
        strings[5] = null;
    }

    public static void main2(String[] args) {
        HashMap<Integer, String> hashmap = new HashMap<>();
        hashmap.put(null, "");
        hashmap.put(2, null);
    }

    public static void main1(String[] args) {
        Hashtable<Integer, String> hashtable = new Hashtable<>();
        hashtable.put(null, "");
        hashtable.put(2, null);
    }
}
