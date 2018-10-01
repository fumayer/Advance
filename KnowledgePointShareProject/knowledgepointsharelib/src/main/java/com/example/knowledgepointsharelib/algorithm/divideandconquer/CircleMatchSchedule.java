package com.example.knowledgepointsharelib.algorithm.divideandconquer;

/**
 * Created by sunjie on 2018/10/1.
 */

public class CircleMatchSchedule {
    /**
     * 分治算法：循环赛日程表
     * 题目： n支球队，进行循环赛，
     * 要求如下：(1)每支球队必须与其他n-1支球队各赛一次；
     * (2)每支球队一天只能参赛一次；
     * (3)循环赛在n-1天内结束。8支球队7天内赛完
     */
    public static void main(String[] args) {
        int num = 12;
        int[][] schedule = new int[num][num];

        circleMatchSchedule(schedule, num);
        for (int i = 0; i < schedule.length; i++) {
            for (int j = 0; j < schedule.length; j++) {
                System.out.print(" " + schedule[i][j]);
            }
            System.out.println();
        }
    }

    private static void circleMatchSchedule(int[][] schedule, int num) {
        if (num == 1) {
            schedule[0][0] = 1;
        } else {
            int harf = num / 2;
//            填充左上区域，使用递归，直到num=1
            circleMatchSchedule(schedule, harf);
//            现在左上区域已经填充好了，就可以根据左上区域填充其他区域了
//            填充右上区域，都是比左上大num/2
            for (int i = 0; i < harf; i++) {
                for (int j = harf; j < num; j++) {
                    schedule[i][j] = schedule[i][j - harf] + harf;
                }
            }
//            填充左下区域,都是比左上大num/2
            for (int i = harf; i < num; i++) {
                for (int j = 0; j < harf; j++) {
                    schedule[i ][j] = schedule[i- harf][j] + harf;
                }
            }
//            填充右下区域,和左上一样
            for (int i = harf; i < num; i++) {
                for (int j = harf; j < num; j++) {
                    schedule[i][j ] = schedule[i-harf][j-harf];
                }
            }
        }
    }
}
