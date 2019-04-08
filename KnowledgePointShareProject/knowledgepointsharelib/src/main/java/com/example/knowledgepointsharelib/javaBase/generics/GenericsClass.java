package com.example.knowledgepointsharelib.javaBase.generics;

/**
 * Created by sunjie on 2019/4/8.
 */

public class GenericsClass {
    public static void main(String[] args) {
        Animal animal = new Animal("狮子");
        GC<Animal> gc = new GC<>(animal);
        gc.judge(animal);
        System.out.println(gc.reJudge("string" ));
    }

}


/**
 * 泛型类GC
 * 定义的泛型可以在类中的任何地方使用：变量，构造器，方法。。
 */
class GC<T> {
    T t;

    public GC(T t) {
        this.t = t;
    }

    public void judge(T value) {
        if (value.equals(t)) {
            System.out.println("是同一个对象");
        }
    }

    /**
     * 注意这个<T>, 因为这里重新定义过了，所以参数T 可以是任意其他类型
     * 也就是说，这个<T>覆盖了类的T，这里可以是其他类型
     */
    public <T> boolean reJudge(T value) {
        if (value.equals(t)) {
           return true;
        }
        return false;
    }
}

/**
 * 未传入泛型实参时，在声明继承类的时候，需将泛型的声明也一起加到类中
 */
class Sgc<T> extends GC<T>{
    public Sgc(T t) {
        super(t);
    }
}
class Sgc2  extends GC<Animal>{
    public Sgc2(Animal animal) {
        super(animal);
    }
}

class Animal {
    String name;

    public Animal(String name) {
        this.name = name;
    }
}


