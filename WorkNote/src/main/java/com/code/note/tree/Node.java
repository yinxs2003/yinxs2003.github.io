package com.code.note.tree;

public class Node<T extends Comparable> implements Comparable<Node<T>> {
    public T value;
    public Node left;
    public Node right;

    public Node() {
    }

    public Node(T value) {
        this.value = value;
    }
    @Override
    public int compareTo(Node<T> o) {
        return this.value.compareTo(o.value);
    }
}
