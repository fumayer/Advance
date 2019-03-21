package com.example.knowledgepointsharelib.javaBase.annotation;

/**
 * Created by sunjie on 2019/3/21.
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解中的元注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Inherited
@Documented
public @interface Demo {

}
