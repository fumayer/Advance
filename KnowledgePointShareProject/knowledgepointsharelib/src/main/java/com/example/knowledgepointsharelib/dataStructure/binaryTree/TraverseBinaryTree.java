package com.example.knowledgepointsharelib.dataStructure.binaryTree;

import java.util.Stack;

/**
 * Created by sunjie on 2018/9/7.
 */

public class TraverseBinaryTree {
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

    public TraverseBinaryTree() {
        root = new TreeNode(1, "A");
    }

    public void createBinaryTree() {
        TreeNode nodeB = new TreeNode(2, "B");
        TreeNode nodeC = new TreeNode(3, "C");
        TreeNode nodeD = new TreeNode(4, "D");
        TreeNode nodeE = new TreeNode(5, "E");
        TreeNode nodeF = new TreeNode(6, "F");
        TreeNode nodeG = new TreeNode(7, "G");
        TreeNode nodeH = new TreeNode(8, "H");
        TreeNode nodeI = new TreeNode(9, "I");
        TreeNode nodeX1 = new TreeNode(91, "#");
        TreeNode nodeX2 = new TreeNode(92, "#");
        TreeNode nodeX3 = new TreeNode(93, "#");
        TreeNode nodeX4 = new TreeNode(94, "#");
        TreeNode nodeX5 = new TreeNode(95, "#");
        TreeNode nodeX6 = new TreeNode(96, "#");
        TreeNode nodeX7 = new TreeNode(97, "#");
        TreeNode nodeX8 = new TreeNode(98, "#");
        root.leftChild = nodeB;
        root.rightChild = nodeC;
        nodeB.leftChild = nodeD;
        nodeC.leftChild = nodeE;
        nodeC.rightChild = nodeF;
        nodeD.leftChild = nodeG;
        nodeD.rightChild = nodeH;
        nodeE.rightChild = nodeI;

//        补满
        nodeB.rightChild=nodeX1;
        nodeG.leftChild=nodeX2;
        nodeG.rightChild=nodeX3;
        nodeH.leftChild=nodeX4;
        nodeH.rightChild=nodeX5;
        nodeE.leftChild=nodeX6;
        nodeI.leftChild=nodeX7;
        nodeI.rightChild=nodeX8;
    }

    /**
     * 求二叉树的高度
     */
    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
//            递归左右子树，只要有子节点就自增1，最后比较左右子树取大
            int i = getHeight(node.leftChild);
            int j = getHeight(node.rightChild);
            return (i < j) ? j + 1 : i + 1;
        }
    }

    /**
     * 获取二叉树的结点数
     */

    private int getSize(TreeNode node) {
        if (node == null) {
            return 0;
        } else {
//            有一个结点自增1
            return 1 + getSize(node.leftChild) + getSize(node.rightChild);
        }
    }

    /**
     * 前序遍历——递归
     * 结点不为null，递归左结点，然后递归右结点
     *
     * @author Administrator
     */
    public void preOrder(TreeNode node) {
        if (node != null) {
            System.out.println("preOrder :" + node.getData());
            preOrder(node.leftChild);
            preOrder(node.rightChild);
        }
    }

    /**
     * 前序遍历——非递归
     */

    public void noRecPreOrder(TreeNode node) {
        if (node != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.push(node);
            while (!stack.isEmpty()) {
                //出栈和进栈q
                TreeNode n = stack.pop();//弹出根结点
                //压入子结点:先压入右子结点，再压入左子结点，因为先压入的在底下
                System.out.println("noRecOrder " + n.getData());
                if (n.rightChild != null) {
                    stack.push(n.rightChild);
                }
                if (n.leftChild != null) {
                    stack.push(n.leftChild);
                }
            }
        }
    }

    /**
     * 中序遍历——递归
     *
     * @author Administrator
     */
    public void inorder(TreeNode node) {
        if (node != null) {
            inorder(node.leftChild);
            System.out.println("inorder " + node.getData());
            inorder(node.rightChild);
        }
    }

    /**
     * 中序遍历——非递归
     */

    public void noRecinorder(TreeNode node) {
        if (node != null) {
            Stack<TreeNode> s = new Stack<>();
            // 先从左边开始，一个个放进去，直到叶子，然后node为null，弹出一个结点，读取之后，再放右边的结点
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
    }

    /**
     * 后序遍历——递归
     *
     * @author Administrator
     */
    public void postorder(TreeNode node) {
        if (node == null) {
            return;
        } else {
            postorder(node.leftChild);
            postorder(node.rightChild);
            System.out.println("postorder " + node.getData());
        }
    }

    /**
     * 后序遍历——非递归
     */


    public void nonRecPostOrder(TreeNode root) {
        Stack<TreeNode> s = new Stack<>();
        TreeNode node = root;
        TreeNode lastVisit = root;
        while (node != null || !s.isEmpty()) {
//            将左子树的所有结点入栈
            while (node != null) {
                s.push(node);
                node = node.leftChild;
            }
            //查看当前栈顶元素
            node = s.peek();
            //如果其右子树也为空，或者右子树已经访问，则可以直接输出当前节点的值
            if (node.rightChild == null || node.rightChild == lastVisit) {
                System.out.print(node.data);
                s.pop();
                lastVisit = node;
                node = null;
            } else {
                //否则，继续遍历右子树
                node = node.rightChild;
            }
        }
    }

    public static void main(String[] args) {
        TraverseBinaryTree binaryTree = new TraverseBinaryTree();
        binaryTree.createBinaryTree();
        binaryTree.preOrder(binaryTree.root);
//        binaryTree.nonRecPostOrder(binaryTree.root);
        int size = binaryTree.getSize(binaryTree.root);
        int height = binaryTree.getHeight(binaryTree.root);
        System.out.println("二叉树一共有结点："+size);
        System.out.println("二叉树的高度为："+height);
    }
}