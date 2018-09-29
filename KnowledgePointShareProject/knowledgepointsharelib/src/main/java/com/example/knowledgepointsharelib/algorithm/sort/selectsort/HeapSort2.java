package com.example.knowledgepointsharelib.algorithm.sort.selectsort;

/**
 * Created by sunjie on 2018/9/29.
 */

public class HeapSort2 {
    public static void main(String[] args) {
        int[] ints2 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6};
        int[] ints4 = {-1, 0, 2, 4, 6, 9, 10, 1, 10};
        int[] ints5 = {9, -1, 8, 7, 100, -6, 45, 3, 7};
        int[] ints3 = {26, 53, 67, 48, 57, 13, 48, 32, 60, 9, -1, 8, 7, 100, -6, 45, 3, 7, 50};
        int[] ints = {4,1,3,2,16,9,10,7,8};
        heapSort(ints);
        for (int anInt : ints) {
            System.out.print(" " + anInt);
        }
    }

    /**
     * 堆排序
     */
    private static void heapSort(int[] arr) {
//        将待排序的序列构建成一个最大堆，
        for (int i = arr.length / 2; i >= 0; i--) {
            heapAdjust(arr, i, arr.length);
        }
//        逐步将每个最大值的根节点与末尾元素交换，并且再调整二叉树，使其成为最大堆
        for (int i = arr.length - 1; i > 0; i--) {
//             将堆顶记录和当前未经排序子序列的最后一个记录交换
            swap(arr, 0, i);
//             交换之后，需要重新检查堆是否符合最大堆，不符合则要调整
            heapAdjust(arr, 0, i);
        }
    }

    /**
     * 构建堆的过程
     *
     * @param arr 需要排序的数组
     * @param i   需要构建堆的根节点的序号
     * @param n   数组的长度
     */
    private static void heapAdjust(int[] arr, int i, int n) {
        int child;
        int father;
        for (father = arr[i]; leftChild(i) < n; i = child) {
            child = leftChild(i);
//            如果左子树小于右子树，则需要比较右子树和父节点
            if (child != n - 1 && arr[child] < arr[child + 1]) {
                child++;// 序号增1，指向右子树
            }
//             如果父节点小于孩子结点，则需要交换
            if (father < arr[child]) {
                arr[i] = arr[child];
            } else {
                break; // 大顶堆结构未被破坏，不需要调整
            }
        }
        arr[i] = father;
    }

//        获取到左孩子结点
    private static int leftChild(int i) {
        return 2 * i + 1;
    }

//     交换数组中的元素位置
    private static void swap(int[] arr, int index1, int index2) {
        int tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
    }

}
