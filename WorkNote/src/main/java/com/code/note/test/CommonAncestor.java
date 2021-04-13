package com.code.note.test;

import com.code.note.tree.TreeNode;
import com.code.note.tree.UnOrderBTree;

public class CommonAncestor {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null)
            return null;

        if(p.val ==  root.val || q.val == root.val)
            return root;

        TreeNode left = lowestCommonAncestor(root.left,p,q);
        TreeNode right = lowestCommonAncestor(root.right,p,q);

        if(left != null && right != null)
            return root;
        return left == null ? right : left;
    }

    public static void main(String[] args) {
        UnOrderBTree unOrderBTree = new UnOrderBTree();
        unOrderBTree.insertLevelOrder(new Integer[]{3, 5, 1, 6, 2, 0, 8, null, null, 7, 4});
        unOrderBTree.print();

        CommonAncestor c = new CommonAncestor();
        TreeNode p = new TreeNode(6);
        TreeNode q = new TreeNode(8);
        TreeNode n = c.lowestCommonAncestor(unOrderBTree.getRoot(), p, q);
        System.out.println(n.val);
    }
}
