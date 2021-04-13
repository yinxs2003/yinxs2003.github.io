package com.code.note.test;

import com.code.note.tree.TreeNode;
import com.code.note.tree.UnOrderBTree;
import com.code.note.util.BTreePrinter;

public class MirrorTree {
    public TreeNode mirrorTree(TreeNode root) {

        if (root == null) {
            return null;
        }

        swap(root);

        mirrorTree(root.left);
        mirrorTree(root.right);

        return root;

    }

    private void swap(TreeNode root) {
        TreeNode t = root.left;
        root.left = root.right;
        root.right = t;
    }


    public static void main(String[] args) {
        UnOrderBTree unOrderBTree = new UnOrderBTree();
        unOrderBTree.insertLevelOrder(new Integer[]{
//                1, 3, 4, 5, 6, 7
                4, 2, 7, 1, 3, 6, 9
        });

        unOrderBTree.print();
        TreeNode root = unOrderBTree.getRoot();

        MirrorTree mirrorTree = new MirrorTree();
        TreeNode root2 = mirrorTree.mirrorTree(root);
        BTreePrinter.printNode(root2);
        System.out.println("hello world");
    }
}
