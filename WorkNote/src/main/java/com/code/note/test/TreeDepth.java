package com.code.note.test;

import com.code.note.tree.TreeNode;
import com.code.note.tree.UnOrderBTree;

import static java.lang.Math.max;

public class TreeDepth {

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }return max(maxDepth(root.left), maxDepth(root.right)) + 1;

    }


    public static void main(String[] args) {
        UnOrderBTree unOrderBTree = new UnOrderBTree();

        Integer[] nodeArr = {3, 9, 20, null, null, 15, 7};
        unOrderBTree.insertLevelOrder(nodeArr);
        unOrderBTree.print();


        TreeDepth t = new TreeDepth();
        int depth = t.maxDepth(unOrderBTree.getRoot());

        System.out.println(depth);
    }
}
