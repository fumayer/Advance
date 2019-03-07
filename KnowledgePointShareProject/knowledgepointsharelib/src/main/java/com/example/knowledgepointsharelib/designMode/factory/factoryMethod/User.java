package com.example.knowledgepointsharelib.designMode.factory.factoryMethod;

import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.IOperation;
import com.example.knowledgepointsharelib.designMode.factory.factoryMethod.factory.AddOpFactory;
import com.example.knowledgepointsharelib.designMode.factory.factoryMethod.factory.IFactory;
import com.example.knowledgepointsharelib.designMode.factory.factoryMethod.factory.SubOpFactory;

/**
 * Created by sunjie on 2019/3/6.
 */

public class User {
    public static void main(String[] args) {

        IFactory opFactory = new AddOpFactory();
        IOperation addOperation = opFactory.getOperation();
        int i = addOperation.getResult(100, 123);
        System.out.println("加法结果：" + i);

        opFactory = new SubOpFactory();
        IOperation subOperation = opFactory.getOperation();
        int j = subOperation.getResult(100, 123);
        System.out.println("减法结果：" + j);
    }
}
