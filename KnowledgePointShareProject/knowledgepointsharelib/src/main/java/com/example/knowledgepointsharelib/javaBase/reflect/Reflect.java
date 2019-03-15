package com.example.knowledgepointsharelib.javaBase.reflect;

/**
 * Created by sunjie on 2019/3/15.
 */

public class Reflect {
    private String name;
    public int age;
    public static String SEX = "MAN";
    private static String GF = "GEGE";

    static {
    }

    private static String getHobby() {
        return "抽烟喝酒烫头";
    }

    public static String getMarry() {
        return "NO";
    }

    public Reflect() {
    }

    public Reflect(String name) {
        this.name = name;
    }

    private Reflect(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
