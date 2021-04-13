package com.code.note.test;

import com.code.note.tree.TreeNode;
import com.code.note.util.BTreePrinter;

import java.util.LinkedList;
import java.util.Queue;

public class DFSTest {
    private TreeNode root;

    public void initTreeFromList(Integer[] arr) {
        root = initTreeFromList(arr, root, 0);
    }

    public TreeNode initTreeFromList(Integer[] arr, TreeNode x, int n) {
        if (n<arr.length) {
            x = new TreeNode(arr[n]);
            x.left = initTreeFromList(arr, x.left, n * 2 + 1);
            x.right = initTreeFromList(arr, x.right, n * 2 + 2);
        }

        return x;
    }

    public void DFS(TreeNode x) {

        if (x == null) {
            return;
        }

        System.out.print(" " + x.val);
        DFS(x.left);
        DFS(x.right);
    }

    public void BFS(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode x = q.poll();
            if (x.left != null) {
                q.offer(x.left);
            }

            if (x.right != null) {
                q.offer(x.right);
            }
            System.out.print(" " + x.val);
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public static void main(String[] args) {
        Integer[] arr = {5, 3, 6, 2, 4, null, null, 1};
        DFSTest dfsTest = new DFSTest();
        dfsTest.initTreeFromList(arr);
        TreeNode root = dfsTest.getRoot();


        dfsTest.DFS(root);
        dfsTest.BFS(root);
        BTreePrinter.printNode(root);

    }
}
