package com.example.knowledgepointsharelib.javaBase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解成员的赋值:value
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Demo5 {
    String value();
    int age();

}

class Test5 {
//    注解有多个成员，value不能省略
    @Demo5(value = "大黄",age = 1)
    public void go() {

    }
}