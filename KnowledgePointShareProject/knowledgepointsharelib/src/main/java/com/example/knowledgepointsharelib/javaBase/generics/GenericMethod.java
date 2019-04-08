package com.example.knowledgepointsharelib.javaBase.generics;

/**
 * Created by sunjie on 2019/4/8.
 */

public class GenericMethod {
    public static void main(String[] args) {
        Fish fish = new Fish("金鱼");
        GM<Fish> gm = new GM<>(fish);
        gm.judge(fish);
        gm.gm("string");
    }

}

/**
 * 泛型类
 */
class GM<T> {
    T t;

    public GM(T t) {
        this.t = t;
    }

//    不是泛型方法
    public T getType() {
        return t;
    }

//    也不是泛型方法
    public void judge(T t2) {
        if (t2.equals(t)) {
            System.out.println("是同一个对象");
        }
    }


    /**
     * 在修饰符与返回值之间的<T>必不可少，这表明这是一个泛型方法，并且声明了一个泛型T
     * 这个T可以出现在这个泛型方法的任意位置
     * 这个T和类的T没有关系，
     */
    public <T> void gm(T value) {
        if (!value.equals(t)) {
            System.out.println("这个T和类的T没关系");
        }
    }
    public <X> void gm2(X value) {
        if (!value.equals(t)) {
            System.out.println("这个T和类的T没关系");
        }
    }
}

class Fish {
    String name;
    public Fish(String name) {
        this.name = name;
    }
}
