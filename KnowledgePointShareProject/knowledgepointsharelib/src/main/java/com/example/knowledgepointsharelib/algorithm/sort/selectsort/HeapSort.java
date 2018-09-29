package com.example.knowledgepointsharelib.algorithm.sort.selectsort;

/**
 * Created by sunjie on 2018/9/29.
 */

public class HeapSort {
    public static void main(String[] args) {
        int[] ints2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6};
        int[] ints4 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints5 = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        int[] ints = {26, 53, 67, 48, 57, 13, 48, 32, 60, 9, -1, 8, 7, 100, -6, 45, 3, 7, 50};
        int[] ints3 = {26, 53, 67, 48, 57, 13, 48, 32, 60, 50};
        heapSort(ints);
        for (int anInt : ints) {
            System.out.print(" " + anInt);
        }

    }

    public static void heapSort(int [] a){
        if(a == null||a.length<=1){
            return;
        }
        //创建大堆
        buildMaxHeap(a);
        for(int i = a.length-1;i>=1;i--){
            //最大元素已经排在了下标为0的地方
            exchangeElements(a, 0, i);//每交换换一次，就沉淀一个大元素
            maxHeap(a, i, 0);
        }
    }


    private static void buildMaxHeap(int[] a) {
        int half = (a.length -1)/2;//假设长度为9
        for(int i = half;i>=0;i--){
            //只需遍历43210
            maxHeap(a,a.length,i);
        }
    }

    //length表示用于构造大堆的数组长度元素数量
    private static void maxHeap(int[] a, int length, int i) {
        int left = i*2+1;
        int right = i*2+2;
        int largest = i;
        if(left<length&&a[left]>a[i]){
            largest = left;
        }
        if(right<length&&a[right]>a[largest]){
            largest = right;
        }
        if(i!=largest){
            //进行数据交换
            exchangeElements(a,i,largest);
            maxHeap(a, length, largest);
        }
    }

    //在数组a里进行两个下标元素交换
    private static void exchangeElements(int[] a, int i, int largest) {
        int temp = a[i];
        a[i] = a[largest];
        a[largest] = temp;
    }
}
