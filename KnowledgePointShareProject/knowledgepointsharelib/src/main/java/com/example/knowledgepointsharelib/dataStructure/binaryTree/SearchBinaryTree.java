package com.example.knowledgepointsharelib.dataStructure.binaryTree;

/**
 * Created by sunjie on 2018/9/19.
 */

public class SearchBinaryTree {
    public class TreeNode {
        private int data;
        private TreeNode leftChild;
        private TreeNode rightChild;
        private TreeNode parent;

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public TreeNode(int data) {
            this.data = data;
            this.leftChild = null;
            this.rightChild = null;
        }
    }

    public TreeNode root;

    public void inorder(TreeNode node) {
        if (node != null) {
            inorder(node.leftChild);
            System.out.print(node.data + " ");
            inorder(node.rightChild);
        }
    }

    /**
     * 插入结点
     */
    public void insert(int data) {
        TreeNode node = null;
        TreeNode parent = null;
//      第一个放进来的确定为根节点，然后之后放进来的不是左就是右，没有可能会替代父节点的情况（相同不处理）
        if (root == null) {
            root = new TreeNode(data);
            return;
        }
        node = root;
//       从根节点开始找，如果大于就往右小于就往左，循环之后，最终left或者right变成null，说明新进来的结点就在这个结点之下的左或者右结点
        while (node != null) {
            parent = node;
            if (data > node.data) {
                node = node.rightChild;
            } else if (data < node.data) {
                node = node.leftChild;
            } else {
                return;
            }
        }
//        创建该结点，上面已经确定了父节点，所以直接挂在父节点的左或者右就可以了
        node = new TreeNode(data);
        if (data < parent.data) {
            parent.leftChild = node;
        } else {
            parent.rightChild = node;
        }
        node.parent = parent;
        return;
    }


    /**
     * 查找结点
     */
    public TreeNode searchNode(int data) {
        TreeNode node = root;
        if (node != null) {
//            从根节点开始遍历，大于就往右，小于就往左，直到找到该结点
            while (node != null) {
                if (data < node.data) {
                    node = node.leftChild;
                } else if (data > node.data) {
                    node = node.rightChild;
                } else {
                    return node;
                }
            }
        }
        return null;
    }


    /**
     * 获取后继结点
     * 后继结点就是右子树中最小的结点，也就是右子树的最左节点
     * 没有右子树就找父节点，如果自己是父节点的右结点，就自己作为父节点，父节点作为父节点的父节点，循环找，直到找到自己是父节点的左节点为止
     */
    public TreeNode getNextNode(TreeNode node) {
        if (node == null) {
            return null;
        } else {
            if (node.rightChild != null) {
//                找某一个结点的最小结点
                return getMinTreeNode(node.rightChild);
            } else {
                TreeNode parent = node.parent;
                while (parent != null && node == parent.rightChild) {
                    node = parent;
                    parent = parent.parent;
                }
                return parent;
            }

        }

    }

    /**
     * 获取该结点作为根节点之下的最小结点，也就是最左结点
     */
    private TreeNode getMinTreeNode(TreeNode node) {
        if (node == null) {
            return null;
        } else {
            while (node.leftChild != null) {
                node = node.leftChild;
            }
        }
        return node;
    }


    public void deleteNode(int data) {
        TreeNode node = searchNode(data);
        if (node == null) {
            throw new NullPointerException("?????");
        } else {
            deleteNode(node);
        }
    }

    public void deleteNode(TreeNode node) {
        TreeNode parent = node.parent;
//       无左无右，直接删
        if (node.leftChild == null && node.rightChild == null) {
            if (parent.leftChild == node) {
                parent.leftChild = null;
            }
            if (parent.rightChild == node) {
                parent.rightChild = null;
            }
            return;
        }
//          有左无右，父节点连接该结点的左节点
        if (node.leftChild != null && node.rightChild == null) {
            if (parent.leftChild == node) {
                parent.leftChild = node.leftChild;
            } else {
                parent.rightChild = node.leftChild;
            }
            return;
        }
//         有右五左，父节点连接该结点的右节点
        if (node.leftChild == null && node.rightChild != null) {
            if (parent.leftChild == node) {
                parent.leftChild = node.rightChild;
            } else {
                parent.rightChild = node.rightChild;
            }
            return;
        }
//        左右都有（找后继结点<后继结点找右子树的最小值，没有右子树，找父结点是作为爷爷结点的左子树存在的，那么爷爷就是后继结点>）
//        后继结点一定是叶子结点，直接把数据替换就可以了
        TreeNode next = getNextNode(node);
        deleteNode(next);
        node.data = next.data;
    }
  /*          11

      4               98

  3     5         66

              54     78

           22   34


    */

    public static void main(String[] args) {
        SearchBinaryTree binaryTree = new SearchBinaryTree();
        int[] ints = {11, 4, 5, 98, 66, 54, 78, 22, 34, 64, 3};
        for (int i : ints) {
            binaryTree.insert(i);
        }
        binaryTree.inorder(binaryTree.root);
        binaryTree.deleteNode(11);
        System.out.println();
        binaryTree.inorder(binaryTree.root);

    }
}
