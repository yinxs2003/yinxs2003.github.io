package com.code.note.tree;

import com.code.note.listnode.ListNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinaryTreeTest {
    public static void main(String[] args) {

        BinaryTree<Integer> binaryTree = new BinaryTree<Integer>();
        binaryTree.put(3);
        binaryTree.put(5);
        binaryTree.put(1);
        binaryTree.put(0);

        binaryTree.put(2);
        binaryTree.put(4);

        binaryTree.put(10);


        binaryTree.traverse();

        int count = binaryTree.count();
        log.error("count:{}", count);

        int depth = binaryTree.getDepth();
        log.error("depth:{}", depth);


        // 翻转二叉树
        binaryTree.print();
//        binaryTree.reverse();
//        binaryTree.print();

        // 二叉树转链表
        ListNode listNode = binaryTree.toListNode();
        listNode.traverse();
    }
}
