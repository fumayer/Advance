package com.example.knowledgepointsharelib.algorithm.recursion;

/**
 * Created by sunjie on 2018/9/30.
 * 欧几里得算法：最大公约数
 */

public class GreatestCommonDivisor {
    public static void main(String[] args) {
        int greatestCommonDivisor = greatestCommonDivisor(3, 9);
        System.out.println(greatestCommonDivisor);
    }

    /**
     * 求两个数的最大公约数
     */
    private static int greatestCommonDivisor(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return greatestCommonDivisor(b, a % b);
        }
    }
}
