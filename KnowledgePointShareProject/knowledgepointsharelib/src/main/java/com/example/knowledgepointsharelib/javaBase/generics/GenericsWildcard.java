package com.example.knowledgepointsharelib.javaBase.generics;

/**
 * Created by sunjie on 2019/4/8.
 */

public class GenericsWildcard {
    public static void main(String[] args) {
        GW<String> gw = new GW<>("");  // 可以直接传确定的类型实参 String
        GW<?> gw1 = new GW<>("");      // 类型不确定，用无界限通配符？，

//        类型不太确定，但是肯定是CharSequence或其子类，所以用 上界通配符<? extends CharSequence>
        GW<? extends CharSequence> gw2 = new GW<>("");

//        类型不太确定，但是肯定是String或其父类，所以用 下界通配符<? super String>
        GW<? super String> gw3 = new GW<>("");
    }
}

class GW<T> {
    T t;

    public GW(T t) {
        this.t = t;
    }

}

