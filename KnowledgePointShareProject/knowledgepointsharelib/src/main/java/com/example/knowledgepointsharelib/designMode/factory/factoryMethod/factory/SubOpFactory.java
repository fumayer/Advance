package com.example.knowledgepointsharelib.designMode.factory.factoryMethod.factory;

import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.AddOperation;
import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.IOperation;
import com.example.knowledgepointsharelib.designMode.factory.easyFactory.object.SubOperation;

/**
 * Created by sunjie on 2019/3/6.
 */

public class SubOpFactory implements IFactory {

    @Override
    public IOperation getOperation() {
        return new SubOperation();
    }
}
