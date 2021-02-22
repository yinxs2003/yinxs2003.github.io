package com.code.note.tree;

import com.code.note.listnode.ListNodeExample;
import com.code.note.util.BTreePrinter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderTree<T extends Comparable> {
    private TreeNode<T> root;

    public void put(T value) {
        root = put(root, value);
    }

    private TreeNode<T> put(TreeNode<T> x, T value) {
        if (x == null) {
            return new TreeNode<>(value);
        }
        int cmp = value.compareTo(x.val);
        if (cmp < 0) {
            x.left = put(x.left, value);
        } else if (cmp > 0) {
            x.right = put(x.right, value);
        } else {
            return x;
        }

        return x;
    }

    public void print() {
        BTreePrinter.printNode(root);
    }

    public void traverse() {
        traverse(root);
    }

    public int count() {
        return count(root);
    }

    private int count(TreeNode x) {
        if (x == null) {
            return 0;
        }
        return 1 + count(x.left) + count(x.right);
    }

    private void traverse(TreeNode node) {
        // 前序遍历
        if (node == null) return;
        log.info("{}", node.val);
        traverse(node.left);
        // 中序遍历
        traverse(node.right);
        // 后序遍历
    }

    //反转二叉树
    public void reverse() {
        reverse(root);
    }

    private void reverse(TreeNode x) {

        if (x == null) return;

        TreeNode tmp = x.left;
        x.left = x.right;
        x.right = tmp;

        reverse(x.left);
        reverse(x.right);

    }

    // 树得深度
    public int getDepth() {
        return getDepth(root);
    }

    private int getDepth(TreeNode x) {
        if (x == null) return 0;
        return 1 + Math.max(getDepth(x.left), getDepth(x.right));
    }

    public TreeNode getRoot() {
        return root;
    }

    ListNodeExample<Comparable> listNode = new ListNodeExample<>();

    public ListNodeExample toListNode() {

        toListNode(root);
        return listNode;
    }

    // 删除节点
    public void delete(T value) {

    }

    // 树转单链表
    private void toListNode(TreeNode node) {
        // 前序遍历
        if (node == null) return;
        listNode.addToTail(node.val);

        toListNode(node.left);
        // 中序遍历
        toListNode(node.right);
        // 后序遍历
    }

    // 合并两棵树
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return null;
        }

        int val1 = t1 == null ? (int) t1.val : 0;
        int val2 = t2 == null ? (int) t2.val : 0;

        int value = val1 + val2;
        TreeNode node = new TreeNode(value);
        node.left = mergeTrees(t1.left, t2.left);
        node.right = mergeTrees(t2.right, t2.right);
        return node;

    }
}

