package com.example.knowledgepointsharelib.designMode.template;

/**
 * Created by sunjie on 2018/10/13.
 */

public class Client {
    public static void main(String[] args) {
        Dishes dishes1=new CaiXin();
        dishes1.cook();
        Dishes dishes2=new BaoCai();
        dishes2.cook();
    }
}
