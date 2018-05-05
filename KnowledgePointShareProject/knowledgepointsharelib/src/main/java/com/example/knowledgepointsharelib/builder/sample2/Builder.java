package com.example.knowledgepointsharelib.builder.sample2;

/**
 * Created by sunjie on 2018/5/5.
 */

public  abstract class Builder {

    //第一步：装CPU
//声明为抽象方法，具体由子类实现
    public abstract void  BuildCPU();

    //第二步：装主板
//声明为抽象方法，具体由子类实现
    public abstract void BuildMainboard();

    //第三步：装硬盘
//声明为抽象方法，具体由子类实现
    public abstract void BuildHD();

    //返回产品的方法：获得组装好的电脑
    public abstract Computer GetComputer();
}
