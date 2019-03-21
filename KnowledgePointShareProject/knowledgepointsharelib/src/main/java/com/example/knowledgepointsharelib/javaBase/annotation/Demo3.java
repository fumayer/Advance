package com.example.knowledgepointsharelib.javaBase.annotation;

import com.example.knowledgepointsharelib.encryption.md5.Client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解中的成员的类型
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Demo3 {
//    8种基本数据类型，包装类不行
    int age(); //Integer ag();
    boolean marry();

//  四种其他类型：String Class Enum Anotation
    String name() default "";
    Class cl();//类
    Em em(); //枚举
    Demo anno(); // 注解

//    以上12种类型的数组  Person 是不行的
    int[] ins();
    String[] sts();
    Em[] ems();
}

