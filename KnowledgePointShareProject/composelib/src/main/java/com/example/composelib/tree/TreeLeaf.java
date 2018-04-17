package com.example.composelib.tree;

import com.example.composelib.LogUtil;

/**
 * Created by sunjie on 2018/4/16.
 */

public class TreeLeaf extends TreeComponent {
    public TreeLeaf(String name) {
        super(name);
    }

    @Override
    public void doSomething() {
        LogUtil.e("TreeLeaf", "14-----doSomething--->" + name);
    }


}
