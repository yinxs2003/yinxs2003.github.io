package com.code.note.tree;

public class BinaryTreeTest {
    public static void main(String[] args){

        BinaryTree<Integer> binaryTree = new BinaryTree<Integer>();
        binaryTree.put(3);
        binaryTree.put(5);
        binaryTree.put(1);
        binaryTree.put(10);


        binaryTree.traverse();

    }
}
