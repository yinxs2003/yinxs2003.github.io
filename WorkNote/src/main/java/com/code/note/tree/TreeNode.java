package com.code.note.tree;

public class TreeNode<T extends Comparable> implements Comparable<TreeNode<T>> {
    public T val;
    public TreeNode<T> left;
    public TreeNode<T> right;

    public TreeNode() {
    }

    public TreeNode(T val) {
        this.val = val;
    }

    public TreeNode(T val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(TreeNode<T> o) {
        return this.val.compareTo(o.val);
    }

    @Override
    public String toString() {
        return " " + val;
    }
}
