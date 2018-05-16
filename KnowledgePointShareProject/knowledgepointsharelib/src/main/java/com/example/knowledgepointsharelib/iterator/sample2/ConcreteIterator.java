package com.example.knowledgepointsharelib.iterator.sample2;

import java.util.List;

/**
 * Created by sunjie on 2018/5/13.
 */

public class ConcreteIterator<T> implements Iterator<T> {
    private List<T> list;
    private int cursor;

    public ConcreteIterator(List<T> list) {
        this.list = list;
    }

    @Override
    public T netx() {
        return list.get(cursor++);
    }

    @Override
    public boolean hasNext() {
        return cursor != list.size();
    }
}
