package com.example.knowledgepointsharelib.designMode.factory.factoryMethod.factory;

import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.AddOperation;
import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.IOperation;

/**
 * Created by sunjie on 2019/3/6.
 */

public class AddOpFactory implements IFactory {

    @Override
    public IOperation getOperation() {
        return new AddOperation();
    }
}
