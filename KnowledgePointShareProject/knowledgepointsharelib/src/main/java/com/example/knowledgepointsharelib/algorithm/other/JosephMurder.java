package com.example.knowledgepointsharelib.algorithm.other;

/**
 * Created by sunjie on 2018/10/5.
 */

public class JosephMurder {
    static class Person {
        int position;
        Person next;

        public Person(int position) {
            this.position = position;
        }
    }

    public static final int PERSONNUMBER = 20;//总人数
    public static final int GAP = 5;//杀人间隔

    public static void main(String[] args) {
//    创建对应数量的首尾相连的人
        Person header = new Person(1);
        Person x = header;
        for (int i = 2; i <= PERSONNUMBER; i++) {
            x.next = new Person(i);
            x = x.next;
        }
        x.next = header;

//        如果header != header.next，说明至少还有两个人
        while (header != header.next) {
//            从一数到GAP，就把这个人的下一个杀掉，然后将这个人的下一个人赋值为下下一个人
            for (int i = 1; i < GAP; i++) {
                header = header.next;
            }
            System.out.println("被干掉的是： " + header.next.position);
            header.next = header.next.next;
        }
        System.out.println("最终幸存的人数： " + header.position);
    }
}
