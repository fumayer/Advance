package com.example.knowledgepointsharelib.javaBase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解成员的赋值
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Demo4 {
    // 没有默认值，强制要值
    int age();
    String name();

    // 有默认值，不会强制要值
    boolean marry() default false;

}

class Test {
    // 键值对形式赋值
    @Demo4(age = 18, name = "大黄")
    public void go() {

    }
}

