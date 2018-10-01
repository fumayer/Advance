package com.example.knowledgepointsharelib.algorithm.greed;

/**
 * Created by sunjie on 2018/10/1.
 */

public class Backpack {
    public static void main(String[] args) {
        int[] weights = {5, 35, 45, 50, 15, 25,10}; //物品重量
        int[] values = {10, 40, 50, 25, 20, 30,5};   //对应价值
        int backpackCapcity = 150; //背包容量
        backpack(weights, values, backpackCapcity);
    }

    private static void backpack(int[] weights, int[] values, int backpackCapcity) {
        int num = weights.length;
        double[] costPerformance = new double[num];//记录性价比的数组
        int[] index = new int[num];// 记录性价比对应的重量、价值的角标
        for (int i = 0; i < num; i++) {
            costPerformance[i] = (double) values[i] / weights[i];
            index[i] = i;
        }
//         给性价比排序，相应的角标数组的位置做对应变化
        for (int i = 0; i < costPerformance.length - 1; i++) {
            for (int j = 0; j < costPerformance.length - 1 - i; j++) {
                if (costPerformance[j] < costPerformance[j + 1]) {
                    double temp = costPerformance[j];
                    costPerformance[j] = costPerformance[j + 1];
                    costPerformance[j + 1] = temp;
                    int tempI = index[j];
                    index[j] = index[j + 1];
                    index[j + 1] = tempI;
                }
            }
        }

//        此时角标数组中的角标就是性价比高的顺序，for循环遍历，然后往背包放东西，直到放不下
        for (int i = 0; i < index.length; i++) {
            int currentWeight = weights[index[i]];
            int currentValue = values[index[i]];
            if (backpackCapcity > currentWeight) {
//                可以放
                backpackCapcity -= currentWeight;
                System.out.println("放进了背包重量："+currentWeight+"   价值："+currentValue);
            }
        }
        System.out.println("背包剩余容量："+backpackCapcity);
    }
}
