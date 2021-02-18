package com.code.note.tree;

import java.util.ArrayList;
import java.util.List;

public class KthLargestVal {
    private List<Integer> kthLargestValList = new ArrayList<>();
    int ans, cnt;

    public int kthLargest2(TreeNode root, int k) {

        if (root == null) {
            return 0;
        }

        kthLargestNode2(root, k);
        Integer a = kthLargestValList.get(k - 1);
        System.out.println(a);
        return a;
    }

    /**
     * 二叉搜索树特点：中序遍历是有序的
     *
     * @param x
     */

    public void kthLargestNode2(TreeNode x, int k) {
        if (x == null) {
            return;
        }

        kthLargestNode2(x.right, k);
        kthLargestValList.add((Integer) x.val);
        if (kthLargestValList.size() == k) {
            return;
        }
        kthLargestNode2(x.left, k);
    }


    public int kthLargest(TreeNode root, int k) {

        if (root == null) {
            return 0;
        }

        kthLargestNode(root, k);
        System.out.println(ans);
        return ans;
    }


    public void kthLargestNode(TreeNode x, int k) {
        if (x == null) {
            return;
        }

        kthLargestNode(x.right, k);
        if (++cnt == k) {
            ans = (int) x.val;
            return;
        }
        kthLargestNode(x.left, k);

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

        node.kthLargest2(root, 1);

    }
}
