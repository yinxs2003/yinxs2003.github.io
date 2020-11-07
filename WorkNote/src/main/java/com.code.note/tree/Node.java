package com.code.note.tree;

public class Node<T extends Comparable>{
    T value;
    Node left;
    Node right;

    Node(T value) {
        this.value = value;
    }
}
