package com.example.knowledgepointsharelib.algorithm.sort.exchangesort;

/**
 * Created by sunjie on 2018/9/29.
 */

public class QuickSort {
    public static void main(String[] args) {
        int[] ints3 = {9, 7, 5, 3, 1, 0, -1, -2};
        int[] ints2 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        quickSort(ints, 0, ints.length - 1);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    public static void quickSort(int[] ints, int start, int end) {
        if (ints != null && ints.length > 1) {
            if (start < end) {
//                找到整个数组第一个元素的正确位置
//                然后递归找 数组开端到该元素前和该元素后到数组结尾，完成排序
                int baseValue = getFirstIndex(ints, start, end);
                quickSort(ints, start, baseValue - 1);
                quickSort(ints, baseValue + 1, end);

            }
        }

    }

    /**
     * 将数组的left和right区域的第一个元确定其正确位置，并且将位置返回
     */
    private static int getFirstIndex(int[] ints, int left, int right) {
        int temp = ints[left];
        while (left < right) {
//           第一个元素和和right元素比较，如果right大，说明在right左边，那么right--
//           如果遇到了right小，那么就将该值赋值给left，
//           下一轮循环进来就又会接着从这个位置找，不断让left和right靠近，最终重合
            while (left < right && ints[right] >= temp) {
                right--;
            }
            ints[left] = ints[right];
//           第一个元素和和left元素比较，如果left小，说明在left右边，那么left++
//           如果遇到了left大，那么就将该值赋值给right，
//           下一轮循环进来就又会接着从这个位置找，不断让left和right靠近，最终重合

            while (left < right && ints[left] <= temp) {
                left++;
            }
            ints[right] = ints[left];
        }
//        left的位置，就是第一个元素的正确位置
        ints[left] = temp;
        return left;

    }

    /**
     * 将数组的left和right区域的第一个元确定其正确位置，并且将位置返回
     */
    private static int getFirstIndex1(int[] ints, int left, int right) {
        int temp = ints[left];
        while (left < right) {
            while (left < right && ints[right] >= temp) {
                right--;
            }
            ints[left] = ints[right];

            while (left < right && ints[left] <= temp) {
                left++;
            }
            ints[right] = ints[left];
        }
        ints[left] = temp;
        return left;
    }

}
