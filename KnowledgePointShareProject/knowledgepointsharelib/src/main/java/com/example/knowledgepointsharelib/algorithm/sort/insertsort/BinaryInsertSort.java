package com.example.knowledgepointsharelib.algorithm.sort.insertsort;

/**
 * Created by sunjie on 2018/9/28.
 */

public class BinaryInsertSort {
    public static void main(String[] args) {
        int[] ints3 = {9, 7, 5, 3, 1, 0, -1, -2};
        int[] ints2 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        binaryInsertSort(ints);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    private static void binaryInsertSort(int[] ints) {
        if (ints != null && ints.length > 1) {
            for (int i = 1; i < ints.length; i++) {
                int temp = ints[i];
                int left = 0;
                int right = i - 1;
                int mid = 0;

                while (left <= right) {
                    mid = (left + right) / 2;
                    if (temp < ints[mid]) {
//                        说明应该继续往左找
                        right = mid - 1;
                    } else {
//                        说明应该继续往右找，就算是相等，直接+1就能到正确的插入角标
                        left = mid + 1;
                    }
                }
//       left>right 一种就是right一直往左走(是一直)，这种情况只能是right最后变成0让后-1，
//                  另一种就是left往右走，所以left肯定就是我们将要插入的位置，而不能够使用right
//                  我们需要把left到之间的元素，全部向后移一位
                for (int j = i; j > left; j--) {
                    ints[j] = ints[j - 1];
                }
                if (left != i) {
                    ints[left] = temp;
                }
            }
        }
    }
}
