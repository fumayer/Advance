package com.example.knowledgepointsharelib.designMode.builder.sample2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/5/5.
 */

public class Computer{

    //电脑组件的集合
    private List<String> parts = new ArrayList<String>();

    //用于将组件组装到电脑里
    public void Add(String part){
        parts.add(part);
    }

    public void Show(){
        for (int i = 0;i<parts.size();i++){
            System.out.println("组件"+parts.get(i)+"装好了");
        }
        System.out.println("电脑组装完成，请验收");


    }

}
