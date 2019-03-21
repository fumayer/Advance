package com.example.knowledgepointsharelib.javaBase.annotation.use.demo2;

/**
 * Created by sunjie on 2019/3/21.
 */

public class Client {
    public void ziwojieshao() {
        System.out.println("我写的程序没有 bug!");
    }

    @Check
    public void suanShu() {
        System.out.println("1234567890");
    }

    @Check
    public void jiafa() {
        System.out.println("1+1=" + 1 + 1);
    }

    @Check
    public void jiefa() {
        System.out.println("1-1=" + (1 - 1));
    }

    @Check
    public void chengfa() {
        System.out.println("3 x 5=" + 3 * 5);
    }

    @Check
    public void chufa() {
        System.out.println("6 / 0=" + 6 / 0);
    }


    public static void main(String[] args) {
        Client client = new Client();
        JSQ.jsq(client);
    }
}
