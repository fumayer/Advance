package com.example.composelib.tree;

import com.example.composelib.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/4/16.
 */

public class TreeBranch extends TreeComponent {
    private List<TreeComponent> treeComponents = new ArrayList<>();

    public TreeBranch(String name) {
        super(name);
    }

    @Override
    public void doSomething() {
        LogUtil.e("TreeBranch", "19-----doSomething--->" + name);
        for (TreeComponent treeComponent : treeComponents) {
            treeComponent.doSomething();
        }
    }

    public void addTreeCom(TreeComponent treeComponent) {
        treeComponents.add(treeComponent);
    }

    public void removeTreeCom(TreeComponent treeComponent) {
        treeComponents.remove(treeComponent);
    }
}
