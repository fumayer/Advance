package com.example.knowledgepointsharelib.adapter.sample2.classadapter;

/**
 * Created by sunjie on 2018/5/31.
 */

public class Test {
    public static void main(String[] args) {
        System.out.println("===============类适配器==============");
//        Mobile mobile = new Mobile();
//        mobile.charging(new VoltageAdapter());
        VoltageAdapter adapter = new VoltageAdapter();
        if (adapter != null) {
            System.out.println("插上充电器充电");
            if (adapter.output5V() == 5) {
                System.out.println("电压刚刚好5V，开始充电");
            } else {
                System.out.println("电压不是5V，都闪开 我要变成note7了");
            }
        }
    }
}
