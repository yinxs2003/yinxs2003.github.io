package com.code.note.tree;

import java.util.LinkedList;
import java.util.Queue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tree<T extends Comparable> {
    private Node<T> root;

    public void put(T value) {
        root = put(root, value);
    }

    public Node put(Node x, T val) {
        if (val == null) {
            return null;
        }

        if (x == null) {
            return new Node<>(val);
        }

        Node<T> valNode = new Node<>(val);
        int cmp = x.compareTo(valNode);
        if (cmp > 0) {
            return put(x.left, val);
        } else if (cmp < 0) {
            return put(x.right, val);
        } else {
            return valNode;
        }
    }

    private Queue<Node<T>> q = new LinkedList<>();
    private static Tree tree;

    private void BFS() {

        q.offer(root);
        while (!q.isEmpty()) {
            Node<T> t = q.poll();

            if (t.left != null) {
                q.offer(t.left);
            }

            if (t.right != null) {
                q.offer(t.right);
            }

            log.info("{}", t.value);
        }
    }

    public Node<T> getRoot() {
        return root;
    }

    public int getDepth(Node root) {

        return 0;
    }

    public static void main(String[] args) {
        tree = new Tree();
        Integer[] arr = { 3, 9, 20, null, null, 15, 7 };
        for (Integer a : arr) {
            tree.put(a);
        }

        // tree.BFS();
        BTreePrinter.printNode(tree.getRoot());
    }
}
