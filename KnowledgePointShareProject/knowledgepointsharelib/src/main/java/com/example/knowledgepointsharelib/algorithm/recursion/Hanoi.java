package com.example.knowledgepointsharelib.algorithm.recursion;

/**
 * Created by sunjie on 2018/9/30.
 */

public class Hanoi {
    private static int count = 1;

    public static void main(String[] args) {
        hanoi(4, 'A', 'B', 'C');
    }

    private static void hanoi(int amount, char from, char depend, char to) {
        if (amount == 0) {
            return;
        }
        if (amount == 1) {
            move(1, from, to);

        } else {
            hanoi(amount - 1, from, to, depend);//将最底层的盘子以上的先移动到过度柱子
            move(amount, from, to);                     //移动最底层盘子到最终的柱子
            hanoi(amount - 1, depend, from, to);//现在过度柱子作为起始柱子，将原先的起始柱子作为过度柱子，
                                                        // 将盘子移动到最终柱子
        }

    }

    private static void move(int position, char from, char to) {
        System.out.println("第" + count++ + "步:盘子" + position + "从" + from + "移动到" + to);
    }
}
