package com.example.knowledgepointsharelib.javaBase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sunjie on 2019/3/21.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Demo6 {
//    注解只有一个成员，最好用value表示，使用的时候方便
    String value();
}

class Test6 {
    //    注解只有一个成员，value可以省略
    @Demo6("大黄")
    public void go() {

    }
}
