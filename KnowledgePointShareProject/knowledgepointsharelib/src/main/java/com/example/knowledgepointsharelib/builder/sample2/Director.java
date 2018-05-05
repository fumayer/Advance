package com.example.knowledgepointsharelib.builder.sample2;

/**
 * Created by sunjie on 2018/5/5.
 */

public class Director {
    //指挥装机人员组装电脑
    public void Construct(Builder builder) {

        builder.BuildCPU();
        builder.BuildMainboard();
        builder.BuildHD();
    }


}
