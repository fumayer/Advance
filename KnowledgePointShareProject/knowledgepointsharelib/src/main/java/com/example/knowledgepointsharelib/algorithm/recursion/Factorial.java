package com.example.knowledgepointsharelib.algorithm.recursion;

/**
 * Created by sunjie on 2018/9/30.
 */

public class Factorial {
    public static void main(String[] args) {
        long number = 20;
        long factorialByFor = calculateFactorialByFor(number);
        System.out.println("for循环求"+number+"的阶乘为：" + factorialByFor);

        long factorialByRecursion = calculateFactorialByRecursion(number);
        System.out.println("递归方式求"+number+"的阶乘为：" + factorialByRecursion);
    }


    /**
     * for循环求阶乘 5！=5*4*3*2*1
     * 注意j和I的类型，int的话，14之后会越界
     */
    private static long calculateFactorialByFor(long i) {
        if (i == 0||i == 1) {
            return 1;
        }
        if (i < 0) {
            return -1;
        }
        for (long j = i - 1; j > 0; j--) {
            i *= j;
        }
        return i;
    }

    /**
     * 递归求阶乘 5！=5*4！ 4！=4*3！ 3！=3*2！2！=2*1！
     * 返回值是long
     */
    private static long calculateFactorialByRecursion(long i) {
        if (i == 0 || i == 1) {
            return 1;
        }
        if (i < 0) {
            return -1;
        }
        return i * calculateFactorialByRecursion(i - 1);
    }
}
