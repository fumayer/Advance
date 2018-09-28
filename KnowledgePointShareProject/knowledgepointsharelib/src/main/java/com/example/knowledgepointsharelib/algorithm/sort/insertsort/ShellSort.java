package com.example.knowledgepointsharelib.algorithm.sort.insertsort;

/**
 * Created by sunjie on 2018/9/28.
 */

public class ShellSort {
    public static void main(String[] args) {
        int[] ints2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6};
        int[] ints4 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints5 = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        int[] ints = {26, 53, 67, 48, 57, 13, 48, 32, 60, 9, -1, 8, 7, 100, -6, 45, 3, 7, 50};
        int[] ints3 = {26, 53, 67, 48, 57, 13, 48, 32, 60, 50};
        shellSort(ints);
//        shellSort2(ints);
//        shellSortReview(ints);
        for (int anInt : ints) {
            System.out.print(" " + anInt);
        }

    }


//          对数组分组并对每个组做直接插入排序,完成后缩小增量分组变大, 重复插入排序, 直到变为一个组
    public static void shellSort(int[] ints) {
        int section = ints.length;
//        先按一定的区间进行分组，这里采用的是数组长度/2，最终为1
        while (section > 0) {
            section /= 2;
//           假设数组长度为16，
//           第一次就是以8开始遍历，8，9，10，11，12，13，14，15
//           第二次就是以4开始遍历，4，5，6，7，8，9，10，11，12，13，14，15
//           第三次就是以2开始遍历，2，3，4，5，6，7，8，9，10，11，12，13，14，15
//           第四次就是以1开始遍历，1，2，3，4，5，6，7，8，9，10，11，12，13，14，15
            for (int i = section; i < ints.length; i++) {
                int temp = ints[i];
                int j = i - section;
//                以区间为单位进行分组，然后进行直接插入排序
//                第一次是以8为区间，那么就是[8,0] [9,1] [10,2] [11,3] [12,4] [13,5] [14,6] [15,7] 每个组内将第一个插入组中正确位置
//                第二次是以4为区间，那么就是[4,0] [5,1]... [8,4,0] [9,5,1]... [12,8,4,0] [13,9,5,1] ...每个组内将第一个插入组中正确位置
//                第三次是以2为区间，那么就是[2,0] [3,1]... [4,2,0] [5,3,1]... [12,8,4,2,0] [13,9,5,3,1] ...每个组内将第一个插入组中正确位置
//                第四次是以1为区间，那么就是[1,0] [2,1,0] [3,2,1,0] ...[15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0]...每个组内将第一个插入组中正确位置
//                注意这里进行的是直接插入排序，是用我们将要插入的数和分组内的前面的数比较，而不是上一个和上上一个比较，
//                我们就是把分组当成了已经排好序的数组(尽管它其实并没有排好)，所以我们往前比的时候，只要大了，立即停止
                while (j >= 0 && temp < ints[j]) {
                    ints[j + section] = ints[j];
                    j -= section;
                }
//                我们将要插入的数，插入到上面比较停止的位置
                ints[j + section] = temp;
            }
        }
    }

    public static void shellSort1(int[] data) {
        int j = 0;
        int temp = 0;
        int increment = data.length / 2;
        while (increment > 0) {
            System.out.print("increment:" + increment + " --->");
            for (int i = increment; i < data.length; i++) {
                // System.out.println("i:" + i);
                temp = data[i];
                for (j = i - increment; j >= 0; j -= increment) {
                    // System.out.println("j:" + j);
                    // System.out.println("temp:" + temp);
                    // System.out.println("data[" + j + "]:" + data[j]);
                    if (temp < data[j]) {
                        data[j + increment] = data[j];
                    } else {
                        break;
                    }
                }
                data[j + increment] = temp;
            }
            increment /= 2;
            for (int datum : data) {
                System.out.printf(" " + datum);
            }
            System.out.println("");
        }
    }

    private static void shellSortReview(int[] ints) {
        int section = ints.length;
        while (section > 0) {
            section /= 2;
            for (int i = section; i < ints.length; i++) {
                int temp = ints[i];
                int j = i - section;
                while (j >= 0 && temp < ints[j]) {
                    ints[j + section] = ints[j];
                    j -= section;
                }
                ints[j + section] = temp;
            }
        }
    }

}