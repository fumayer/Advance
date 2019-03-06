package com.example.knowledgepointsharelib.designMode.factory.easyFactory;

import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.IOperation;

/**
 * Created by sunjie on 2019/3/6.
 */

public class User {
    public static void main(String[] args) {
        IOperation addOperation = Factory.getOperation('+');
        int i = addOperation.getResult(100, 123);
        System.out.println("加法：" + i);
        IOperation minusOperation = Factory.getOperation('-');
        int j = minusOperation.getResult(100, 123);
        System.out.println("减法：" + j);
    }
}
