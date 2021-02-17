package com.code.note.tree;

import java.util.ArrayList;
import java.util.List;

public class KthLargestVal {
    private List<Integer> kthLargestValList = new ArrayList<>();

    public int kthLargest(TreeNode root, int k) {

        if (root == null) {
            return 0;
        }

        kthLargestNode(root);
        Integer a = kthLargestValList.get(kthLargestValList.size() - k);
        System.out.println(a);
        return a;
    }

    /**
     *
     * 二叉搜索树特点：中序遍历是有序的
     *
     * @param x
     */

    public void kthLargestNode(TreeNode x) {
        if (x == null) {
            return;
        }

        kthLargestNode(x.left);
        kthLargestValList.add((Integer) x.val);
        kthLargestNode(x.right);
    }


    public static void main(String[] args) {
        OrderTree t = new OrderTree();
        Integer arr[] = {5, 3, 6, 2, 4, null, null, 1};
        for (Integer a : arr) {
            if (a != null) {
                t.put(a);
            }
        }

        TreeNode root = t.getRoot();
        t.print();

        KthLargestVal node = new KthLargestVal();
        node.kthLargest(root, 1);
    }
}
