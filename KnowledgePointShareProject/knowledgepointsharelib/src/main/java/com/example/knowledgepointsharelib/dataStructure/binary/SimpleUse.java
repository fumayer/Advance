package com.example.knowledgepointsharelib.dataStructure.binary;

import java.util.Stack;

/**
 * Created by sunjie on 2018/9/7.
 */

public class SimpleUse {
    private TreeNode root = null;

    public SimpleUse() {
        root = new TreeNode(1, "A");
    }

    /**
     * 构建二叉树
     * A
     * B       C
     * D      E        F
     */
    public void createBinaryTree() {
        TreeNode nodeB = new TreeNode(2, "B");
        TreeNode nodeC = new TreeNode(3, "C");
        TreeNode nodeD = new TreeNode(4, "D");
        TreeNode nodeE = new TreeNode(5, "E");
        TreeNode nodeF = new TreeNode(6, "F");
        root.leftChild = nodeB;
        root.rightChild = nodeC;
        nodeB.leftChild = nodeD;
        nodeB.rightChild = nodeE;
        nodeC.rightChild = nodeF;
    }

    /**
     * 求二叉树的高度
     *
     * @author Administrator
     */
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
            int i = getHeight(node.leftChild);
            int j = getHeight(node.rightChild);
            return (i < j) ? j + 1 : i + 1;
        }
    }

    /**
     * 获取二叉树的结点数
     *
     * @author Administrator
     */
    public int getSize() {
        return getSize(root);
    }


    private int getSize(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + getSize(node.leftChild) + getSize(node.rightChild);
        }
    }

    /**
     * 前序遍历——迭代
     *
     * @author Administrator
     */
    public void preOrder(TreeNode node) {
        if (node == null) {
            return;
        } else {
            System.out.println("preOrder data:" + node.getData());
            preOrder(node.leftChild);
            preOrder(node.rightChild);
        }
    }

    /**
     * 前序遍历——非迭代
     */

    public void nonRecPreOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(node);
        while (!stack.isEmpty()) {
            //出栈和进栈
            TreeNode n = stack.pop();//弹出根结点
            //压入子结点
            System.out.println("nonRecOrder data" + n.getData());
            if (n.rightChild != null) {
                stack.push(n.rightChild);
            }
            if (n.leftChild != null) {
                stack.push(n.leftChild);
            }
        }
    }

    /**
     * 中序遍历——迭代
     *
     * @author Administrator
     */
    public void midOrder(TreeNode node) {
        if (node == null) {
            return;
        } else {
            midOrder(node.leftChild);
            System.out.println("midOrder data:" + node.getData());
            midOrder(node.rightChild);
        }
    }

    /**
     * 中序遍历——非迭代
     */

    public void nonRecMidOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        Stack<TreeNode> s = new Stack<TreeNode>();
        while (node != null || !s.empty()) {
            if (node != null) {
                s.push(node);
                node = node.leftChild;
            } else {
                node = s.pop();
                System.out.println(node.data);
                node = node.rightChild;
            }
        }

    }

    /**
     * 后序遍历——迭代
     *
     * @author Administrator
     */
    public void postOrder(TreeNode node) {
        if (node == null) {
            return;
        } else {
            postOrder(node.leftChild);
            postOrder(node.rightChild);
            System.out.println("postOrder data:" + node.getData());
        }
    }

    /**
     * 后序遍历——非迭代
     */

    public void nonRecPostOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        Stack<TreeNode> s = new Stack<TreeNode>();

        TreeNode curNode; //当前访问的结点
        TreeNode lastVisitNode; //上次访问的结点
        curNode = node;
        lastVisitNode = node;

        //把currentNode移到左子树的最下边
        while (curNode != null) {
            s.push(curNode);
            curNode = curNode.leftChild;
        }
        while (!s.empty()) {
            curNode = s.pop();  //弹出栈顶元素
            //一个根节点被访问的前提是：无右子树或右子树已被访问过
            if (curNode.rightChild != null && curNode.rightChild != lastVisitNode) {
                //根节点再次入栈
                s.push(curNode);
                //进入右子树，且可肯定右子树一定不为空
                curNode = curNode.rightChild;
                while (curNode != null) {
                    //再走到右子树的最左边
                    s.push(curNode);
                    curNode = curNode.leftChild;
                }
            } else {
                //访问
                System.out.println(curNode.data + " ");
                //修改最近被访问的节点
                lastVisitNode = curNode;
            }
        } //while

    }

    public class TreeNode {
        private int index;
        private String data;
        private TreeNode leftChild;
        private TreeNode rightChild;


        public int getIndex() {
            return index;
        }


        public void setIndex(int index) {
            this.index = index;
        }


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


    public static void main(String[] args) {
        SimpleUse binaryTree = new SimpleUse();
        binaryTree.createBinaryTree();
//        int height = binaryTree.getHeight();
//        System.out.println("treeHeihgt:" + height);
//        int size = binaryTree.getSize();
//        System.out.println("treeSize:" + size);
//		binaryTree.preOrder(binaryTree.root);
//		binaryTree.midOrder(binaryTree.root);
//		binaryTree.postOrder(binaryTree.root);
//        binaryTree.nonRecPreOrder(binaryTree.root);
//        binaryTree.nonRecMidOrder(binaryTree.root);
        binaryTree.nonRecPostOrder(binaryTree.root);
    }
}