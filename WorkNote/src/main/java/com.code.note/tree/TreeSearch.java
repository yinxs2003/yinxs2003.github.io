package com.code.note.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TreeSearch {

    public Node initTree() {
        BinaryTree<Integer> binaryTree = new BinaryTree<Integer>();
        binaryTree.put(3);
        binaryTree.put(5);
        binaryTree.put(1);
        binaryTree.put(2);
        binaryTree.put(10);
        binaryTree.put(0);
        binaryTree.put(4);

        Node root = binaryTree.getRoot();

        BTreePrinter.printNode(root);

        return root;
    }

    /**
     * 深度优先 - 递归
     */
    public void DFS() {
        Node root = initTree();
        DFS(root);
    }


    public void DFS(Node x) {
        if (x == null) return;
        System.out.print(x.value + " ");
        DFS(x.left);
        DFS(x.right);
    }

    public void BFS() {
        Queue<Comparable> res = new LinkedList<>();
        Queue<Node> q = new LinkedList<>();

        Node root = initTree();

        // 入队
        q.offer(root);
        while (!q.isEmpty()) {
            // 出队
            Node e = q.poll();
            if (e == null) {
                return;
            }

            if (e.left != null) {
                q.offer(e.left);
            }

            if (e.right != null) {
                q.offer(e.right);
            }

//            res.offer(e.value);
            System.out.print(e.value + " ");
        }
//        System.out.println(res);
    }

    public static void main(String[] args) {

        TreeSearch treeSearch = new TreeSearch();
        treeSearch.BFS();
        System.out.println();

        treeSearch.DFS();
    }
}
