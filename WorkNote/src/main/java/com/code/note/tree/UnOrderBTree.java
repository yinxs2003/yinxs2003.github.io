package com.code.note.tree;

import com.code.note.util.BTreePrinter;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 无序二叉树
 */
public class UnOrderBTree {
    private TreeNode root;

    public TreeNode insertLevelOrder(int[] arr, TreeNode x, int i) {
        if (i < arr.length) {
            x = new TreeNode(arr[i]);
            x.left = insertLevelOrder(arr, x.left, 2 * i + 1);
            x.right = insertLevelOrder(arr, x.right, 2 * i + 2);
        }
        return x;
    }

    public boolean isSymmetric(TreeNode root) {
        Queue<TreeNode> checkQueue = new LinkedList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        boolean isSymmetric = false;
        while (!q.isEmpty()) {
            TreeNode x = q.poll();

            if (x.left != null) {
                q.offer(x.left);

            }

            if (x.right != null) {
                q.offer(x.right);
            }

            if (x.left != null || x.right != null) {
                checkQueue.add(x.left);
                checkQueue.add(x.right);
            }

            isSymmetric = check(checkQueue);
        }

        return isSymmetric;
    }

    public boolean check(Queue<TreeNode> checkQueue) {
        if (checkQueue.size() > 2) {
            while (!checkQueue.isEmpty()) {
                TreeNode x1 = ((LinkedList<TreeNode>) checkQueue).peekFirst();
                TreeNode x2 = ((LinkedList<TreeNode>) checkQueue).peekLast();

                if (x1 == null && x2 == null) {
                    ((LinkedList<TreeNode>) checkQueue).pollFirst();
                    ((LinkedList<TreeNode>) checkQueue).pollLast();
                } else if (x1 != null && x2 != null) { // 都不为空，且不相等
                    if (x1.val != x2.val) {
                        printStack(checkQueue);
                        return false;
                    } else {
                        return true;
                    }
                } else { // 有一个不为空，返回false
                    return false;
                }
            }
        }
        return true;
    }

    public void printStack(Queue<TreeNode> checkQueue) {
        for (TreeNode n : checkQueue) {
            if (n == null) {
                System.out.print(" null ");
            } else {
                System.out.print(" " + n.val);
            }
        }
        System.out.println();
    }


    public static void main(String args[]) {
        UnOrderBTree t2 = new UnOrderBTree();
        int arr[] = {1, 2, 3, 4, 5, 6, 6, 6, 6};
        t2.root = t2.insertLevelOrder(arr, t2.root, 0);

        BTreePrinter.printNode(t2.root);

    }
} 