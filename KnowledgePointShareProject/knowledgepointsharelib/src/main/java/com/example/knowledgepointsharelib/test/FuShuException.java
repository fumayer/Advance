package com.example.knowledgepointsharelib.test;

/**
 * 自定义的异常：除数不能是负数
 */
public class FuShuException extends Exception {
    private int value;

    public FuShuException(String msg, int value) {
        super(msg);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

class Client {
    /**
     * 调用计算方法，捕获自定义异常
     */
    public static void main(String[] args) {
        try {
            int x = calc(4, -1);
            System.out.println("x:" + x);
        } catch (FuShuException e) {
            System.out.println("错误的负是：" + e.getValue());
            e.printStackTrace();
        }
        System.out.println("over");
    }

    /**
     * 进行计算的方法，如果除数是负数，就抛出自定义的异常
     */
    public static int calc(int a, int b) throws FuShuException {
        if (b < 0) {
            //手动通过throw关键字抛出一个自定义异常对象。
            throw new FuShuException("出现了除数是负数的情况", b);
        }
        return a / b;
    }
}
