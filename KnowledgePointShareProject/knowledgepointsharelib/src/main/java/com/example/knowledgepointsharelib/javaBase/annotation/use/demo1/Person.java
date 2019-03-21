package com.example.knowledgepointsharelib.javaBase.annotation.use.demo1;

/**
 * Created by sunjie on 2019/3/21.
 */

public class Person {
    @AssignString("大黄")
    private String name;
    @AssignInt(18)
    private int age;

    public static void main(String[] args) throws IllegalAccessException {
        Person person = new Person();
        JSQ.jsq(person);
        System.out.println("姓名："+person.name+"   年龄："+person.age);
    }
}
