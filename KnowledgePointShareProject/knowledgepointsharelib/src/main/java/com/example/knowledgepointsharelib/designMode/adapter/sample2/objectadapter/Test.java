package com.example.knowledgepointsharelib.designMode.adapter.sample2.objectadapter;

import android.view.animation.ScaleAnimation;

/**
 * Created by sunjie on 2018/5/31.
 */

public class Test {
    public static void main(String[] args) {
        System.out.println("===============对象适配器==============");
        VoltageAdapter adapter = new VoltageAdapter(new Voltage220());
        if (adapter != null) {
            System.out.println("插上充电器充电");
            if (adapter.output5V() == 5) {
                System.out.println("电压刚刚好5V，开始充电");
            } else {
                System.out.println("电压不是5V，都闪开 我要变成note7了");
            }
        }
        ScaleAnimation animation = new ScaleAnimation(null, null);
        if (animation!=null) {

        }
    }
}
