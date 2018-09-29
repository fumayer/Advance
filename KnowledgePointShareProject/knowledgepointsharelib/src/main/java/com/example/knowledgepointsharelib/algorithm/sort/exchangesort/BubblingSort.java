package com.example.knowledgepointsharelib.algorithm.sort.exchangesort;

import java.util.Arrays;

/**
 * Created by sunjie on 2018/9/29.
 */

public class BubblingSort {
    public static void main(String[] args) {
        int[] ints3 = {9, 7, 5, 3, 1, 0, -1, -2};
        int[] ints2 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        bubblingSort(ints);
        System.out.println(Arrays.toString(ints));
    }

    private static void bubblingSort(int[] ints) {
//        外层循环一次，每次冒出一个，最后一个就不需要冒了，就是你
        for (int i = 0; i < ints.length - 1; i++) {
//           如果内层循环没有执行冒泡，说明已经排序完成，break
            boolean trigger = true;
//            内层循环-1：最后一个已经没有后一个了，不能比较
//            内层循环-：外层循环一次，一个已经冒泡了，就无需再和他比较了，
            for (int j = 0; j < ints.length - 1 - i; j++) {
                if (ints[j] > ints[j + 1]) {
                    int temp = ints[j];
                    ints[j] = ints[j + 1];
                    ints[j + 1] = temp;
                    trigger = false;
                }
            }
            if (trigger) {
                break;
            }
        }
    }

}
