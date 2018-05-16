package com.example.knowledgepointsharelib.iterator.sample2;

/**
 * Created by sunjie on 2018/5/13.
 */

public interface Aggregation<T> {
    void add(T t);

    void remove(T t);

    Iterator<T> iterator();

}
