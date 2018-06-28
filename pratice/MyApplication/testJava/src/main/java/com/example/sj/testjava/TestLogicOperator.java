package com.example.sj.testjava;

/**
 * Created by sunjie on 2018/6/27.
 */

public class TestLogicOperator {
    public static void main(String[] args) {
        int a = 0;
        System.out.println(1 + 2 * 3);
        System.out.println(true || true && (a++ == 1));
        System.out.println(a);

        int x = 0, y = 1;
        for (int i = 0; i < 11; i++) {
            int temp = y;
            y = y + x;
            x = temp;
        }
        System.out.println(y);
    }
}
