package com.example.knowledgepointsharelib.algorithm.other;

/**
 * Created by sunjie on 2018/10/16.
 */

public class ClockwiseMatrix {
    //    /顺时针打印一个矩阵
    public void test() {
        int[][] num = new int[4][4];
        int count = 1;
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num.length; j++) {
                num[i][j] = count++;
            }
        }
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num.length; j++) {
                System.out.print(" " + num[i][j] + " ");
            }
            System.out.println();
        }
        output(num, 0, num.length - 1);
    }

    public void output(int[][] num, int start, int end) {
        if (start >= end || end <= 0) {
            return;
        }
        for (int i = start; i <= end; i++) {  //输出四阶矩阵的第一行的元素
            System.out.print(num[start][i] + " ");
        }
        for (int i = start + 1; i <= end; i++) {  //输出四阶矩阵的第四列的元素（除去第四列第一个元素）
            System.out.print(num[i][end] + " ");
        }
        for (int i = end - 1; i >= start; i--) {  //输出四阶矩阵的第四行的元素（除去第四行最后一个元素）
            System.out.print(num[end][i] + " ");
        }
        for (int i = end - 1; i > start; i--) {  //输出四阶矩阵的第一列的元素（除去第一行的第一个和最后一个元素）
            System.out.print(num[i][start] + " ");
        }
        output(num, start + 1, end - 1);
    }

    public static void main(String[] args) {
        ClockwiseMatrix testSuanfa = new ClockwiseMatrix();
        testSuanfa.test();
    }
}
