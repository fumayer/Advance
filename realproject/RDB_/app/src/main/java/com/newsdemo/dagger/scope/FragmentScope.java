package com.newsdemo.dagger.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */

@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
