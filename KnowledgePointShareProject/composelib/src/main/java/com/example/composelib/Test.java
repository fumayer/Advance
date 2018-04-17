package com.example.composelib;

import com.example.composelib.tree.TreeBranch;
import com.example.composelib.tree.TreeLeaf;
import com.example.composelib.view.ViewGroup;
import com.example.composelib.view.ViewSingle;

/**
 * Created by sunjie on 2018/4/16.
 */

public class Test {
    public static void main(String[] args) {
//        testTree();
        testView();
    }


    private static void testView() {
        ViewGroup linearLayout = new ViewGroup("linearLayout");
        ViewGroup relativeLayout = new ViewGroup("RelativeLayout");
        ViewGroup frameLayout = new ViewGroup("FrameLayout");
        ViewSingle button = new ViewSingle("Button");
        ViewSingle textView = new ViewSingle("TextView");
        ViewSingle imageView = new ViewSingle("ImageView");
        linearLayout.addView(relativeLayout);
        linearLayout.addView(frameLayout);
        relativeLayout.addView(button);
        frameLayout.addView(textView);
        frameLayout.addView(imageView);

        linearLayout.showView();
    }


    private static void testTree() {
        TreeBranch 树根 = new TreeBranch("树根");
        TreeBranch 树干A = new TreeBranch("树干A");
        TreeBranch 树干B = new TreeBranch("树干B");
        TreeLeaf 树叶a = new TreeLeaf("树叶a");
        TreeLeaf 树叶b = new TreeLeaf("树叶b");
        TreeLeaf 树叶b1 = new TreeLeaf("树叶b1");

        树根.addTreeCom(树干A);
        树根.addTreeCom(树干B);
        树干A.addTreeCom(树叶a);
        树干B.addTreeCom(树叶b);
        树干B.addTreeCom(树叶b1);

        树根.doSomething();
        LogUtil.e("Test", "23-----main-------------------");
        树干A.doSomething();
    }
}
