package com.example.knowledgepointsharelib.algorithm.other;

/**
 * Created by sunjie on 2018/10/16.
 */

public class ArrayGetChildArray {
    public void test() {
        int[] num = {1, 2, 2, 3, 4, 5, 6, 7, 8, 9};
        int sum = 7;
        findSum(num, sum);
    }

    public void findSum(int[] num, int sum) {
        int left = 0;
        int right = 0;
        for (int i = 0; i < num.length; i++) {
            if (i>sum) {
                return;
            }
            int curSum = 0;
            left = i;
            right = i;
            while (curSum < sum) {
                curSum += num[right++];
            }
            if (curSum == sum) {
                for (int j = left; j < right; j++) {
                    System.out.print(num[j] + " ");
                }
                System.out.println();
            }
        }
    }
    public static void main(String[] args) {
        ArrayGetChildArray testSF = new ArrayGetChildArray();
        testSF.test();
    }
}
