package com.example.sunjie.knowledgepointshareproject.builder.sample1;

import com.example.sunjie.knowledgepointshareproject.builder.sample1.pack.Packing;

/**
 * Created by sunjie on 2018/5/5.
 */

public interface Item {
    public String name();

    public Packing packing();

    public float price();
}
