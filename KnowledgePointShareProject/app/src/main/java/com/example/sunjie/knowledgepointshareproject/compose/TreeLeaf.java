package com.example.sunjie.knowledgepointshareproject.compose;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/16.
 */

public class TreeLeaf extends TreeComponent {
    public TreeLeaf(String name) {
        super(name);
    }

    @Override
    void doSomething() {
        LogUtil.e("TreeLeaf", "14-----doSomething--->" + name);
    }
}
