package com.example.knowledgepointsharelib.test;

import java.util.Arrays;

/**
 * Created by sunjie on 2018/10/16.
 */

public class Test2 {
    public void test() {
        //int[] num = {1,-2,3,10,-4,10,2,-5};
        int[] num = {1, -2, 3, 10, -4, 7, 2, -5};
        System.out.println(Arrays.toString(num));
        System.out.println("最终结果是："+maxSum(num));
    }

    public int maxSum(int[] num) {
        int curSum = 0; //运行中求的和
        int curMaxSum = -99999999;//当前最大和
        int start = 0;//起始位置
        int end = 0; // 结束位置
        for (int i = 0; i < num.length; i++) {
//            前面计算的小于0，那么重新从当前位置往后计算(重置)
//            前面计算的大于0，加上当前位置
//            然后比较加上这个位置的数是不是比之前要大，大的话讲当前最大和赋值，位置后移
//            如果没大过的话，进入下一个位置继续比较
            if (curSum <= 0) {
                curSum = num[i];
                start = i;
            } else {
                curSum += num[i];
            }
            if (curSum > curMaxSum) {
                curMaxSum = curSum;
                end = i;
            }
        }
        System.out.println("数组中连续元素可以组成最大值的元素是");
        for (int i = start; i <= end; i++) {
            System.out.print(" "+num[i]);
        }
        return curMaxSum;
    }

    public static void main(String[] args) {
        Test2 test2 = new Test2();
        test2.test();
    }
}
