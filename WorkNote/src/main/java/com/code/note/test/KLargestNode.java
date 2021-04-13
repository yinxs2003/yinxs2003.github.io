package com.code.note.test;

import com.code.note.tree.TreeNode;
import com.code.note.tree.UnOrderBTree;

public class KLargestNode {
    TreeNode treeNode = null;
    int count = 0;

    public int kthLargest(TreeNode root, int k) {

        if (root == null) {
            return -1;
        }

        helper(root, k);

        if (treeNode == null) {
            return -1;
        } else {
            return (int) treeNode.val;
        }
    }


    public void helper(TreeNode root, int k) {

        if (root == null) {
            return;
        }

        helper(root.right, k);

        if (++count == k) {
            treeNode = root;
            return;
        }

        helper(root.left, k);
    }

    public static void main(String[] args) {
        UnOrderBTree unOrderBTree = new UnOrderBTree();
        Integer[] arr = {3, 1, 4, null, 2};
        unOrderBTree.insertLevelOrder(arr);
        TreeNode root = unOrderBTree.getRoot();
        KLargestNode k = new KLargestNode();
        int i = k.kthLargest(root, 1);

        System.out.println(i);
    }
}
