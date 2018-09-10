package com.example.knowledgepointsharelib.designMode.iterator.sample2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/5/13.
 */

public class ConcreteAggregation<T> implements Aggregation<T> {
    private List<T> list = new ArrayList<>();

    @Override
    public void add(T o) {
        list.add(o);
    }

    @Override
    public void remove(T o) {
        list.remove(o);
    }

    @Override
    public Iterator iterator() {
        return new ConcreteIterator<>(list);
    }
}
