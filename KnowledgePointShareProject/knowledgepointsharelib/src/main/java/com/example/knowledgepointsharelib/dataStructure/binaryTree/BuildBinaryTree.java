package com.example.knowledgepointsharelib.dataStructure.binaryTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunjie on 2018/9/19.
 * 构建二叉树
 */

public class BuildBinaryTree {
    public class TreeNode {
        private int index;
        private String data;
        private TreeNode leftChild;
        private TreeNode rightChild;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public TreeNode(int index, String data) {
            this.index = index;
            this.data = data;
            this.leftChild = null;
            this.rightChild = null;
        }
    }

    private TreeNode root = null;

    public TreeNode buildBinaryTree(int size, ArrayList<String> data) {
        if (data.size() == 0) {
            return null;
        }
        String d = data.get(0);
        TreeNode node;
        int index = size - data.size();
        if (d.equals("#")) {
            node = null;
            data.remove(0);
            return null;
        }
        node = new TreeNode(index, d);
        if (index == 0) {
            root = node;
        }
        data.remove(0);
        node.leftChild = buildBinaryTree(size, data);
        node.rightChild = buildBinaryTree(size, data);
        return node;
    }

    public static void main(String[] args) {
        BuildBinaryTree binaryTree = new BuildBinaryTree();
        String[] strings = {"A","B","#","D","#","#","C","#","#"};
//        String[] strings2 = {"A","B","D","G","#","#","#","H","#","#","#","C","E","#","I","#","#","F","#","#",};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(strings));
        binaryTree.buildBinaryTree(list.size(),list);
        binaryTree.preorder(binaryTree.root);
    }

    public void preorder(TreeNode node){
        if (node!=null) {
            System.out.println(node.data);
            preorder(node.leftChild);
            preorder(node.rightChild);
        }
    }
}
