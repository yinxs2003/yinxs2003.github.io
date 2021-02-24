package com.code.note.tree;

public class IsSymmetricTree {

    boolean f = true;

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }

        isSymmetric(root.left, root.right);
        return f;
    }


    private void isSymmetric(TreeNode root1, TreeNode root2) {

        if (root1 == root2) {
            return;
        }

        if (root1 == null || root2 == null) {
            f = false;
            return;
        }

        if (root1.val != root2.val) {
            f = false;
            return;
        }

        isSymmetric(root1.left, root2.right);
        isSymmetric(root1.right, root2.left);
    }


    public static void main(String[] args) {
        UnOrderBTree unOrderBTree = new UnOrderBTree();
        unOrderBTree.insertLevelOrder(new Integer[]{
                1, 2, 2, 4, 3, 3
        });

        unOrderBTree.print();
        TreeNode root = unOrderBTree.getRoot();


        IsSymmetricTree mirrorTree = new IsSymmetricTree();
        boolean flag = mirrorTree.isSymmetric(root);
        System.out.println(flag);
    }
}
