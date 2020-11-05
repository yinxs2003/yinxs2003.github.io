package com.code.note.tree;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class BinaryTreeTest {
    public static void main(String[] args) {

        BinaryTree<Integer> binaryTree = new BinaryTree<Integer>();
        binaryTree.put(3);
        binaryTree.put(5);
        binaryTree.put(1);
        binaryTree.put(2);

        binaryTree.put(10);


        binaryTree.traverse();

        int count = binaryTree.count();
        log.info("count:{}", count);

        int depth = binaryTree.getDepth();
        log.info("depth:{}", depth);


        // 翻转二叉树
        binaryTree.print();
        binaryTree.reverse();
        binaryTree.print();

    }
}
