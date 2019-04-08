package com.example.knowledgepointsharelib.javaBase.generics;

/**
 * Created by sunjie on 2019/4/8.
 */

public class GenericsInf {
    public static void main(String[] args) {
      new IG<Dog>(){
          @Override
          public Dog getType() {
              return null;
          }
      };
    }
}

/**
 * 泛型接口
 */
interface IG<T> {
    T getType();
}

class Dog{
    String name;
}

/**
 * 未传入泛型实参时， 在声明实现类的时候，需将泛型的声明也一起加到类中
 */
class Sig<T> implements IG<T>{
    @Override
    public T getType() {
        return null;
    }
}

class Sig2 implements IG{
    @Override
    public Object getType() {
        return null;
    }
}

