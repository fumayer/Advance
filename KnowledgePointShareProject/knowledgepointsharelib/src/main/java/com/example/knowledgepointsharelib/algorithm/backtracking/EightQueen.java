package com.example.knowledgepointsharelib.algorithm.backtracking;

/**
 * Created by sunjie on 2018/10/5.
 */

public class EightQueen {
    public static void main(String[] args) {
        getCount(0);
    }

    public final static int MAXQUEEN = 8;//皇后数量，棋盘大小n*n
    public static int[] queenLocation = new int[MAXQUEEN];//皇后的位置，queenLocation[3]=5 表示第三列皇后在第五行
    public static int COUNT;// 一共有多少中摆法

    private static void getCount(int col) {
        boolean[] canPut = new boolean[MAXQUEEN];
//        boolean数组默认为true，然后判断各种不可摆放位置，将该位置改为false
        for (int i = 0; i < canPut.length; i++) {
            canPut[i] = true;
        }
        for (int i = 0; i < col; i++) {
//        前面的皇后所在的行不能继续放皇后
            canPut[queenLocation[i]] = false;
            int gap = col - i;
//        前面的皇后的正斜方向不能继续放皇后
            if (queenLocation[i] - gap >= 0) {
                canPut[queenLocation[i] - gap] = false;
            }
//        前面的皇后的反斜方向不能继续放皇后
            if (queenLocation[i] + gap <= MAXQUEEN - 1) {
                canPut[queenLocation[i] + gap] = false;
            }
        }

//        通过我们定义的boolean数组，判断哪里可以放皇后
        for (int i = 0; i < canPut.length; i++) {
            if (canPut[i]) {
                queenLocation[col] = i;
//                如果小于MAXQUEEEN-1，说明还没到最后一行，继续往下，
//                如果等于MAXQUEEEN-1，说明找到了一种方案COUNT++，返回继续寻找下一种方案
                if (col < MAXQUEEN - 1) {
                    getCount(col + 1);
                } else {
                    COUNT++;
                   printQueenLocation();
                }
            }
        }
    }

    private static void printQueenLocation() {
        System.out.println("第" + COUNT + "种方案");
        for (int n = 0; n < MAXQUEEN; n++) {
            for (int m = 0; m < MAXQUEEN; m++) {
                if (n == queenLocation[m]) {
                    System.out.print("0 ");
                } else {
                    System.out.print("+ ");
                }
            }
            System.out.println();
        }
    }
}
