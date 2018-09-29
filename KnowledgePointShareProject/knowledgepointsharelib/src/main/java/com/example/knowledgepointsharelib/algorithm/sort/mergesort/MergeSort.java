package com.example.knowledgepointsharelib.algorithm.sort.mergesort;

/**
 * Created by sunjie on 2018/9/29.
 */

public class MergeSort {
    public static void main(String[] args) {
        int[] ints = {9, 7, 5, 3, 1, 0, -1, -2};
        int[] ints3 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints2 = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        mergeSort(ints, 0, ints.length - 1);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    private static void mergeSort(int[] ints, int left, int right) {
        if (ints != null && ints.length > 1) {
            if (left < right) {
                int mid = (left + right) / 2;
                mergeSort(ints, left, mid);
                mergeSort(ints, mid + 1, right);
                merge(ints, left, mid, right);
            }
        }
    }


    private static void merge1(int[] ints, int left, int mid, int right) {
        int[] tempInts = new int[right - left + 1];
        int tempIntsIndex = 0;
        int arrStartIndex = left;
        int rightStart = mid + 1;

        while (left <= mid && rightStart <= right) {
            if (ints[left] <= ints[rightStart]) {
                tempInts[tempIntsIndex++] = ints[left++];
            } else {
                tempInts[tempIntsIndex++] = ints[rightStart++];
            }
        }
        while (left <= mid) {
            tempInts[tempIntsIndex++] = ints[left++];
        }
        while (rightStart <= right) {
            tempInts[tempIntsIndex++] = ints[rightStart++];
        }
        for (int i = 0; i < tempInts.length; i++) {
            ints[i + arrStartIndex] = tempInts[i];
        }
    }

    private static void merge(int[] ints, int left, int mid, int right) {
        int[] tempInts = new int[right - left + 1];
        int tempIntsIndex = 0;
        int arrStartIndex = left; // 排好序的数组要把值赋值给原数组的起始位置
        int rightStart = mid + 1; // 右边数组的起始位置

//      找出较小值元素放入临时数组中：如果左右数列都没有走到头，那么就一直两两比较，直到有一个走到头
        while (left <= mid && rightStart <= right) {
            if (ints[left] <= ints[rightStart]) {
                tempInts[tempIntsIndex++] = ints[left++];
            } else {
                tempInts[tempIntsIndex++] = ints[rightStart++];
            }
        }
//       处理较长部分：如果是右数列走到头，就把左数列剩余的放进临时数组
        while (left <= mid) {
            tempInts[tempIntsIndex++] = ints[left++];
        }
//       处理较长部分：如果是左数列走到头，就把右数列剩余的放进临时数组
        while (rightStart <= right) {
            tempInts[tempIntsIndex++] = ints[rightStart++];
        }
//         将排好序的数组赋值给原数组对应位置
        for (int i = 0; i < tempInts.length; i++) {
            ints[i + arrStartIndex] = tempInts[i];
        }
    }

}
