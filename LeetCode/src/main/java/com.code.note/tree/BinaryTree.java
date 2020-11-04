package com.code.note.tree;


public class BinaryTree<T extends Comparable> {
    private Node<T> root;

    public void put(T value) {
        root = put(root, value);
    }

    private Node put(Node<T> x, T value) {
        if (x == null) {
            return new Node(value);
        }
        int cmp = value.compareTo(x.value);
        if (cmp < 0) {
            x.left = put(x.left, value);
        } else if (cmp > 0) {
            x.right = put(x.right, value);
        } else {
            return x;
        }

        return x;
    }

    public void traverse() {
        traverseRecursive(root);
    }

    private void traverseRecursive(Node node) {
        // 前序遍历
        if (node == null) return;
        System.out.println(node.value);
        traverseRecursive(node.left);
        // 中序遍历
        traverseRecursive(node.right);
        // 后序遍历
    }
}
