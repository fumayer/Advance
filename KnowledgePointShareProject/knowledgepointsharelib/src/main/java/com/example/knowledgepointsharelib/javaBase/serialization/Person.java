package com.example.knowledgepointsharelib.javaBase.serialization;

import java.io.Serializable;

/**
 * Created by sunjie on 2019/3/16.
 */
//  必须实现序列化接口
public class Person implements Serializable {
    //    UID最好加上，不加上的话，修改class内容之后不能反序列化回来
    private static final long serialVersionUID = 4091614555225125604L;
    private transient String name; // transient修饰的不会被序列化出去
    public int age;
    /*
    静态变量 ANCESTORS
    static：static修饰的不会序列化出去，但是测试发现反序列化回来居然是对的
    那是因为都在同一个机器（而且是同一个进程），因为这个jvm已经把ANCESTORS加载进来了，
    所以你获取的是加载好的ANCESTORS，如果你是传到另一台机器或者你关掉程序重写写个程序反序列化，
    此时因为别的机器或新的进程是重新加载ANCESTORS的，所以ANCESTORS值就是初始值
    */
    public static String ANCESTORS;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age + '}' +
                "\nPerson.ANCESTORS=" + ANCESTORS;

    }
}
