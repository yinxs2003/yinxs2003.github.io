package com.code.note.tree;

import lombok.extern.slf4j.Slf4j;

import javax.swing.tree.TreeNode;

@Slf4j
public class DisOrderBTree<T extends Comparable> {
    private Node root;
    private int size;

    public void add(T value) {
        root = add(root, value);
    }

    /**
     *
     * @param x
     * @param val val不能为空
     * @return
     */
    private Node add(Node x, T val) {
        size++;
        if (x == null) return new Node(val);
        int p = x.value.compareTo(val);
        if (p > 0) {
            x.left = add(x.left, val);
        } else if (p < 0) {
            x.right = add(x.right, val);
        } else {
            x = add(x, val);
        }
        return x;
    }

    public Node getRoot() {
        return root;
    }

    public void transerve() {
        transerve(root);
    }

    private void transerve(Node x) {
        if (x == null) return;
        log.error("====> {}", x.value);
        transerve(x.left);
        transerve(x.right);
    }

    public static void main(String[] args) {
        DisOrderBTree<Integer> disOrderBTree = new DisOrderBTree<>();
        disOrderBTree.add(1);
        disOrderBTree.add(2);
        disOrderBTree.add(3);
        disOrderBTree.transerve();
        BTreePrinter.printNode(disOrderBTree.getRoot());
    }
}
