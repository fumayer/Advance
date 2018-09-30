package com.example.knowledgepointsharelib.algorithm.recursion;

/**
 * Created by sunjie on 2018/9/30.
 */

public class Fibonacci {

    /*
       有一对兔子，从出生后第3个月起每个月都生一对兔子，小兔子长到第三个月后每个月又生一对兔子
       假如兔子都不死，问每个月的兔子总数为多少对
       month1 =1;
       month2 =1;
       month3 = f2+1=2;
       month4 = f3+1=3;
       month5 = f4+1+1=5；
       month6 = f5+1+1+1=8;
       month7 = f6+1+1+1+1+1=13
       …… （一直循环下去）
       month3 = month1+month2;
       month4 = month2+month3;
       month5 = month3+month4;
       */

    public static void main(String args[]) {
        for (int i = 1; i <= 20; i++) {
            System.out.println(i + "个月的兔子数量：" + f(i));
        }
    }

    public static int f(int x) {
        if (x == 1 || x == 2) {
            return 1;
        } else {
            return f(x - 1) + f(x - 2);
        }
    }
}
