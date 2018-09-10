package com.example.knowledgepointsharelib.designMode.builder.sample2;

/**
 * Created by sunjie on 2018/5/5.
 */

//装机人员1
public class ConcreteBuilder extends Builder {
    //创建产品实例
    Computer computer = new Computer();

    //组装产品
    @Override
    public void BuildCPU() {
        computer.Add("组装CPU");
    }

    @Override
    public void BuildMainboard() {
        computer.Add("组装主板");
    }

    @Override
    public void BuildHD() {
        computer.Add("组装主板");
    }

    //返回组装成功的电脑
    @Override
    public Computer GetComputer() {
        return computer;
    }
}
