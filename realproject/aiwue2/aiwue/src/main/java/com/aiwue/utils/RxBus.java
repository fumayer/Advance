package com.aiwue.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 *  RxBus, based on RxJava
 * Created by Yibao on 17/4/11
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class RxBus {
    // 主题
    private final Subject<Object, Object> bus;//这是一个既可以充当观察者也可以充当被观察者的类
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }


    // 提供了一个新的事件
    public void post(Object o) {
        bus.onNext(o);//发送事件o
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
