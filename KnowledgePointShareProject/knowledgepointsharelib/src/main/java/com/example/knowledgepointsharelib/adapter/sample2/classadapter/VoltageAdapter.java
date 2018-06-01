package com.example.knowledgepointsharelib.adapter.sample2.classadapter;

/**
 * Created by sunjie on 2018/5/31.
 * Adapter类：完成220V-5V的转变
 * 通过继承src类，实现 dst 类接口，完成src->dst的适配。
 */
public class VoltageAdapter extends Voltage220 implements Voltage5 {
    @Override
    public int output5V() {
        int src = output220V();
        System.out.println("适配器工作开始适配电压");
        int dst = src / 44;
        System.out.println("适配完成后输出电压：" + dst);
        return dst;
    }
}
