package com.example.knowledgepointsharelib.adapter.sample2.objectadapter;

/**
 * Created by sunjie on 2018/5/31.
 * Adapter类：完成220V-5V的转变
 * 介绍：对象适配器模式：
 * 持有 src类，实现 dst 类接口，完成src->dst的适配。 。以达到解决**兼容性**的问题。
 */
public class VoltageAdapter implements Voltage5 {
    private Voltage220 voltage220;

    public VoltageAdapter(Voltage220 voltage220) {
        this.voltage220 = voltage220;
    }

    @Override
    public int output5V() {
        int src = 0;
        if (voltage220 != null) {
            src = voltage220.output220V();
        }
        System.out.println("适配器工作开始适配电压");
        int dst = src / 44;
        System.out.println("适配完成后输出电压：" + dst);
        return dst;
    }
}
