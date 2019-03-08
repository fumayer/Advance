package com.example.knowledgepointsharelib.designMode.strategy;

/**
 * Created by sunjie on 2019/3/8.
 */

public class VIPPaymentStrategy implements IPaymentStrategy {
    @Override
    public float realityPayment(float num) {
        return (float) (num*0.8);
    }
}
