package com.code.note.tree;

import com.code.note.util.BTreePrinter;

/**
 * 无序二叉树
 */
public class UnOrderBTree {
    private TreeNode root;

    public void insertLevelOrder(Integer[] arr) {
        if (root == null) root = new TreeNode();
        root = insertLevelOrder(arr, root, 0);
    }

    public TreeNode insertLevelOrder(Integer[] arr, TreeNode x, int i) {
        if (i < arr.length) {
            if (arr[i] != null) {
                x = new TreeNode(arr[i]);
                x.left = insertLevelOrder(arr, x.left, 2 * i + 1);
                x.right = insertLevelOrder(arr, x.right, 2 * i + 2);
            }
        }
        return x;
    }

    /**
     * 是否为对称树树
     *
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {

        if (root == null) {
            return true;
        }

        return isSymmetric(root.left, root.right);
    }

    public boolean isSymmetric(TreeNode l, TreeNode r) {
        if (l == null && r == null) {
            return true;
        }

        if (l == null || r == null || l.val != r.val) {
            return false;
        }

        boolean flag1 = isSymmetric(l.left, r.right);
        boolean flag2 = isSymmetric(l.right, r.left);
        return flag1 && flag2;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public static void main(String args[]) {
        UnOrderBTree t2 = new UnOrderBTree();
        Integer arr[] = {1, 2, 2, null, 3, null, 3};
        t2.root = t2.insertLevelOrder(arr, t2.root, 0);

        BTreePrinter.printNode(t2.root);

        boolean res = t2.isSymmetric(t2.root);
        System.out.println(res);

    }

    public void print() {
        BTreePrinter.printNode(root);
    }
}