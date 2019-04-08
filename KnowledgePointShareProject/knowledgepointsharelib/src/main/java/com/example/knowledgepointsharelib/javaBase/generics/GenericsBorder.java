package com.example.knowledgepointsharelib.javaBase.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2019/4/8.
 */

public class GenericsBorder {
    public static void main(String[] args) {
        GBTop<Integer> gbTop = new GBTop<>(1);
        GBTop<Double> gbTop2 = new GBTop<>(1.1);
        GBTop<Float> gbTop3 = new GBTop<>(1.1f);

//        gbTop.sd(new Tree<C>());
        gbTop.sd(new Tree<B>());
        gbTop.sd(new Tree<A>());
    }
}

/**
 * 上边界
 * <T extends Number>,表示只能是Number及其子类
 * <T super Number>，没有这种东西
 */
class GBTop<T extends Number>{
    T t;

    public GBTop(T t) {
        this.t = t;
    }

    /**
     * 下边界
     * <? super B>，表示只能是的B及其父类
     */
    public  void sd( Tree <? super B>  tree){}
}

class Tree <List>{}

class A  {}
class B extends A{}
class C extends B{}





