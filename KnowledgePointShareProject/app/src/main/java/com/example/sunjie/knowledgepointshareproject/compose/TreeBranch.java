package com.example.sunjie.knowledgepointshareproject.compose;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

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
    void doSomething() {
        LogUtil.e("TreeBranch", "14-----doSomething--->" + name);
        for (TreeComponent treeComponent : treeComponents) {
            treeComponent.doSomething();
        }
    }

    public void addTreeComponent(TreeComponent treeComponent) {
        treeComponents.add(treeComponent);
    }

    public void removeTreeComponent(TreeComponent treeComponent) {
        treeComponents.remove(treeComponent);
    }

}
