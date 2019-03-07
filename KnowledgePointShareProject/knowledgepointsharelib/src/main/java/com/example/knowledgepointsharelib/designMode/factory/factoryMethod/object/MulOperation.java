package com.example.knowledgepointsharelib.designMode.factory.factoryMethod.object;

/**
 * Created by sunjie on 2019/3/6.
 */

public class MulOperation implements IOperation {
    @Override
    public int getResult(int i, int j) {
        return i * j;
    }
}
