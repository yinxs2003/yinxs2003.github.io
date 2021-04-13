package com.code.note.test;

import com.code.note.tree.TreeNode;
import com.code.note.tree.UnOrderBTree;

public class BST {
    public boolean isValidBST(TreeNode<Integer> root) {
        if (root == null) {
            return true;
        }

        return isValidBST(root, root);
    }

    public boolean isValidBST(TreeNode<Integer> x, TreeNode<Integer> rootNode) {
        if (x == null) {
            return true;
        }

        if (x.left != null && (x.left.val >= x.val || x.left.val <= rootNode.val)) {
            return false;
        }

        if (x.right != null && (x.right.val <= x.val || x.right.val >= rootNode.val)) {
            return false;
        }

        boolean flag1 = isValidBST(x.left, rootNode);
        boolean flag2 = isValidBST(x.right, rootNode);

        return flag1 && flag2;
    }

    public void dfs1(TreeNode root) {
        if (root == null) {
            return;
        }

        System.out.print(root.val + " ");
        dfs1(root.left);
        dfs1(root.right);
    }

    public void dfs2(TreeNode root) {
        if (root == null) {
            return;
        }

        dfs2(root.left);
        System.out.print(root.val + " ");
        dfs2(root.right);
    }


    public static void main(String[] args) {
        UnOrderBTree unOrderBTree = new UnOrderBTree();
        unOrderBTree.insertLevelOrder(new Integer[]{3,9,20,15,7,19,21});

        unOrderBTree.print();

        BST bst = new BST();
        boolean flag = bst.isValidBST(unOrderBTree.getRoot());

//        System.out.println(flag);

        TreeNode root = unOrderBTree.getRoot();

        bst.dfs1(root);
        System.out.println();
        bst.dfs2(root);

    }
}
