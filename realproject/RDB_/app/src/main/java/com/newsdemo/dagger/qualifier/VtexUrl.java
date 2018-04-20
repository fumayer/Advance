package com.newsdemo.dagger.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Created by jianqiang.hu on 2017/5/11.
 */




@Qualifier
@Documented
@Retention(RUNTIME)
public @interface VtexUrl {
}
