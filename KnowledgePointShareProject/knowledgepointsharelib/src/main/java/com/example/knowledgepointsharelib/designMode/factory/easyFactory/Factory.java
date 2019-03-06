package com.example.knowledgepointsharelib.designMode.factory.easyFactory;

import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.AddOperation;
import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.DivOperation;
import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.IOperation;
import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.MulOperation;
import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.SubOperation;

/**
 * Created by sunjie on 2019/3/6.
 */

public class Factory {
    public static IOperation getOperation(char c) {
        switch (c) {
            case '+':
                return new AddOperation();
            case '-':
                return new SubOperation();
            case '*':
                return new MulOperation();
            case '/':
                return new DivOperation();
            default:
                break;
        }
        return null;
    }
}
