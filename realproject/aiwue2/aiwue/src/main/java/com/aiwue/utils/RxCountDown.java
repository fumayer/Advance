package com.aiwue.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class RxCountDown {
    public static Observable<Integer> countDown(int time)
    {
        if(time<0) time=0;
        final int countTime = time;
        return Observable.interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return countTime-aLong.intValue();
                    }
                }).take(time+1);//只发射前几个元素，表示传过来几就发射几次
    }
}
