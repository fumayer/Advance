package com.example.knowledgepointsharelib.algorithm.divideandconquer;

/**
 * Created by sunjie on 2018/10/1.
 */

public class ChessboardCover {
    public static void main(String[] args) {
        int size = 8; // 棋盘长宽
        int[][] chessborad = new int[size][size];//棋盘
        chessboardCover(chessborad, 0, 1, 0, 0, size);
        for (int i = 0; i < chessborad.length; i++) {
            for (int j = 0; j < chessborad.length; j++) {
                System.out.print(" " + chessborad[i][j]);
            }
            System.out.println();
        }
    }

    private static int type = 0;

    /**
     * @param chessborad，二维数组表示棋盘
     * @param specialRow          特殊点的横坐标
     * @param specialCol          特殊点的纵坐标
     * @param beginRow            棋盘的起始横坐标
     * @param beginCol            棋盘的起始纵坐标
     * @param size                棋盘的大小，必须是2的乘方 2*2，4*4，8*8，16*16
     */
    private static void chessboardCover(int[][] chessborad, int specialRow, int specialCol, int beginRow, int beginCol, int size) {
        if (size == 1) {
            return;
        }
        int n = type = type % 4 + 1;
        int harf = size / 2;
        int midRow = beginRow + harf;
        int midCol = beginCol + harf;

//        特殊点在左上区域
        if (specialRow < midRow && specialCol < midCol) {
            chessboardCover(chessborad, specialRow, specialCol, beginRow, beginCol, harf);
        } else {
//            在左上区域的右下角放一个点，作为左上区域的特殊点，左上递归完成棋盘覆盖
            chessborad[midRow - 1][midCol - 1] = type;
            chessboardCover(chessborad, midRow - 1, midCol - 1, beginRow, beginCol, harf);
        }

//        特殊点在右上区域
        if (specialRow < midRow && specialCol >= midCol) {
            chessboardCover(chessborad, specialRow, specialCol, beginRow, midCol, harf);
        } else {
//            在右上区域的左下角放一个点，作为右上区域的特殊点，右上递归完成棋盘覆盖
            chessborad[midRow - 1][midCol] = type;
            chessboardCover(chessborad, midRow - 1, midCol, beginRow, midCol, harf);
        }

//        特殊点在左下区域
        if (specialRow >= midRow && specialCol < midCol) {
            chessboardCover(chessborad, specialRow, specialCol, midRow, beginCol, harf);
        } else {
//            在左下区域的右上角放一个点，作为左下区域的特殊点，左下递归完成棋盘覆盖
            chessborad[midRow][midCol - 1] = type;
            chessboardCover(chessborad, midRow, midCol - 1, midRow, beginCol, harf);
        }

//        特殊点在右下区域
        if (specialRow >= midRow && specialCol >= midCol) {
            chessboardCover(chessborad, specialRow, specialCol, midRow, midCol, harf);
        } else {
//            在右下区域的左上角放一个点，作为右下区域的特殊点，右下递归完成棋盘覆盖
            chessborad[midRow][midCol] = type;
            chessboardCover(chessborad, midRow, midCol, midRow, midCol, harf);
        }
    }
}
