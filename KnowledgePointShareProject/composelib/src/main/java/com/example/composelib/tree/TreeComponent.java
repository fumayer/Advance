package com.example.composelib.tree;

/**
 * Created by sunjie on 2018/4/16.
 */

public abstract class TreeComponent {
    String name;

    public TreeComponent(String name) {
        this.name = name;
    }

    abstract void doSomething();

}
