package com.code.note.tree;

import com.code.note.util.BTreePrinter;


public class isBalanceTree {
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        int l = getDepth(root.left);
        int r = getDepth(root.right);
        if (Math.abs(l - r) > 1) {
            return false;
        }

        return isBalanced(root.left) && isBalanced(root.right);
    }

    public int getDepth(TreeNode x) {
        if (x == null) {
            return 0;
        }
        return Math.max(getDepth(x.left), getDepth(x.right)) + 1;
    }


    public static void main(String[] args) {
        System.out.println("hello");

        UnOrderBTree unOrderBTree = new UnOrderBTree();
        Integer arr[] = {1, 2, 2, 3, null, null, 3, 4, null, null, 4};
        unOrderBTree.setRoot(unOrderBTree.insertLevelOrder(arr, unOrderBTree.getRoot(), 0));

        TreeNode root = unOrderBTree.getRoot();

        BTreePrinter.printNode(root);

        isBalanceTree treeDepth = new isBalanceTree();
        boolean isBalance = treeDepth.isBalanced(root);
        System.out.println(isBalance);
    }
}
