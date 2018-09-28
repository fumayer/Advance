package com.example.knowledgepointsharelib.algorithm.sort.insertsort;

/**
 * Created by sunjie on 2018/9/28.
 */

public class DirectInsertSort {
    public static void main(String[] args) {
        int[] ints2 = {9, 7, 5, 3, 1, 0, -1, -2};
        int[] ints3 = {-1, 0, 2, 4, 6, 8, 10};
        int[] ints = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        directInsertSort(ints);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    private static void directInsertSort(int[] ints) {
        if (ints != null && ints.length > 1) {
            for (int i = 1; i < ints.length; i++) {
                int temp = ints[i];
                // 已经排序好的位置
                int alreadySortIndex = i - 1;
//                将改循环的数和已经排序好的数(其实就是它前面的数)挨个比较，小的话就让前一个数后移一位
                while (alreadySortIndex >= 0 && temp < ints[alreadySortIndex]) {
                    ints[alreadySortIndex + 1] = ints[alreadySortIndex];
                    alreadySortIndex--;
                }
//                已经排序好的数组的角标和该数角标不一致，说明有过变化，该数就插入此处
                if ((alreadySortIndex + 1) != i) {
                    ints[alreadySortIndex + 1] = temp;
                }
            }
        }
    }
}
