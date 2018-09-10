package com.example.knowledgepointsharelib.designMode.adapter.sample2.classadapter;

/**
 * Created by sunjie on 2018/5/31.
 * Client类：手机 .需要5V电压
 */
public class Mobile {
    /**
     * 充电方法
     *
     * @param voltage5
     */
    public void charging(Voltage5 voltage5) {
        System.out.println("插上充电器充电");
        if (voltage5.output5V() == 5) {
            System.out.println("电压刚刚好5V，开始充电");
        } else if (voltage5.output5V() > 5) {
            System.out.println("电压超过5V，都闪开 我要变成note7了");
        }
    }
}