package com.example.knowledgepointsharelib.algorithm.recursion;

import com.example.knowledgepointsharelib.algorithm.sort.exchangesort.QuickSort;

import java.util.Arrays;

/**
 * Created by sunjie on 2018/9/29.
 */

public class BinarySearch {
    public static void main(String[] args) {
        int[] ints = {-1, 0, 2, 4, 6, 1, 2, 76, 5, 9, 10, 1, 10};

        QuickSort.quickSort(ints, 0, ints.length - 1);// 必须是排序好的数组，才能二分查找
        System.out.println("排序后的数组："+Arrays.toString(ints));

        int searchElemIndex = recursionBinarySearch(5, ints, 0, ints.length - 1);
        System.out.println("递归法二分查找实际返回的角标： " + searchElemIndex);

        int searchElemIndex2 = directBinarySearch(5, ints);
        System.out.println("非递归法二分查找实际返回的角标： " + searchElemIndex2);


    }

    /**
     * 通过while循环，让left和right不断靠近，找到元素
     */
    private static int directBinarySearch(int elem, int[] ints) {
        if (ints != null && ints.length > 0) {
            int left = 0;
            int right = ints.length - 1;

            while (left < right) {
                int mid = (left + right) / 2;
                if (elem == ints[mid]) {
                    System.out.println("非递归法二分查找找到了，角标是：" + mid);
                    return mid;
                }
                if (elem > ints[mid]) {
                    left = mid + 1;
                }
                if (elem < ints[mid]) {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }


    /**
     * 通过递归，查找区间，找到元素
     */
    private static int recursionBinarySearch(int elem, int[] ints, int left, int right) {
        if (ints != null && ints.length > 0) {
            int mid = (left + right) / 2;
            if (elem == ints[mid]) {
                System.out.println("递归法二分查找找到了，角标是：" + mid);
                return mid;

            }
//            要找的元素大于中间数，说明应该往右找
            if (elem > ints[mid]) {
                return recursionBinarySearch(elem, ints, mid + 1, right);
            }
//            要找的元素小于中间数，说明应该往左找
            if (elem < ints[mid]) {
                return recursionBinarySearch(elem, ints, left, mid - 1);
            }
        }
        return -1;
    }


}
