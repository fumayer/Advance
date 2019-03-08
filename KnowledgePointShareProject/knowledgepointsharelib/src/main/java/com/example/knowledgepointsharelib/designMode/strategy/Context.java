package com.example.knowledgepointsharelib.designMode.strategy;

/**
 * Created by sunjie on 2019/3/8.
 */

public class Context {
    private IPaymentStrategy paymentStrategy;

    public void setPaymentStrategy(IPaymentStrategy iPaymentStrategy) {
        paymentStrategy = iPaymentStrategy;
    }

    public float pay(float num) {
        return paymentStrategy.realityPayment(num);
    }
}
