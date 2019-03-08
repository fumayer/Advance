package com.example.knowledgepointsharelib.designMode.strategy;

/**
 * Created by sunjie on 2019/3/8.
 */

public class User {
    public static void main(String[] args) {
        Context context = new Context();
        context.setPaymentStrategy(new NormalPaymentStrategy());
        System.out.println("普通玩家需要付钱：" + context.pay(1000));
        context.setPaymentStrategy(new VIPPaymentStrategy());
        System.out.println("VIP玩家需要付钱：" + context.pay(1000));
        context.setPaymentStrategy(new SuperVIPPaymentStrategy());
        System.out.println("超级VIP玩家需要付钱：" + context.pay(1000));

    }
}
