package com.example.sunjie.knowledgepointshareproject.filter;

/**
 * Created by sunjie on 2018/4/18.
 */

public class Person {
    public static final String MALE = "男";
    public static final String FEMALE = "女";

    private String name;
    private String sex;
    private int age;
    private boolean maritalStatus;
    private int height;

    public Person(String name,String sex, int age, boolean maritalStatus, int height) {
        this.sex = sex;
        this.name = name;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.height = height;
    }


    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public boolean getMaritalStatus() {
        return maritalStatus;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", maritalStatus=" + maritalStatus +
                ", height=" + height +
                '}';
    }
}
