package com.example.knowledgepointsharelib.test;

/**
 * Created by sunjie on 2018/10/13.
 */

public class Volatile {
    static class Person {
        String name;

        public Person(String name) {
            this.name = name;
        }
    }


    public static void main2(String[] args) {
        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    String threadName = getName();
                    System.out.println("这是" + threadName + "线程， 原始数据是：" + str + "   修改为：" + threadName);
                    str = threadName;
                }
            };
            thread.setName("" + i);
            thread.start();
        }
    }

    volatile static String str = "原始数据";

}
