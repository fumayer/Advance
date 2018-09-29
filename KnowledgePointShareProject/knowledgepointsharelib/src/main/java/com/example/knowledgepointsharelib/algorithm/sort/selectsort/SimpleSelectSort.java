package com.example.knowledgepointsharelib.algorithm.sort.selectsort;

/**
 * Created by sunjie on 2018/9/29.
 */

public class SimpleSelectSort {
    public static void main(String[] args) {
        int[] ints2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6};
        int[] ints4 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints5 = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        int[] ints = {26, 53, 67, 48, 57, 13, 48, 32, 60, 9, -1, 8, 7, 100, -6, 45, 3, 7, 50};
        int[] ints3 = {26, 53, 67, 48, 57, 13, 48, 32, 60, 50};
        simpleSelectSort(ints);
        for (int anInt : ints) {
            System.out.print(" " + anInt);
        }

    }

    private static void simpleSelectSort(int[] ins) {
//        每次找最小元素，放在数组最前端，记得数据交换
//        第一次遍历整个数组，将最小值放在0位置
//        第二次遍历除了0号之外的整个数组，将最小值放在1位置
//        第三次遍历除了0，1号之外的整个数组，将最小值放在2位置
//        。。。
        for (int i = 0; i < ins.length; i++) {
            int min = ins[i];
            for (int j = i; j < ins.length; j++) {
                int currentMinIndex = i;
                if (ins[j] < min) {
                    min = ins[j];
                    currentMinIndex = j;
                }
                int temp = 0;
                temp = ins[i];
                ins[i] = ins[currentMinIndex];
                ins[currentMinIndex] = temp;
            }
        }
    }

}
