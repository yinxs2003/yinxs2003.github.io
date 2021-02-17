---
title: 算法-二叉搜索树第K大节点值
date: 2021-02-17
toc: true
tags: 算法
categories: 
- 技术
---

## 题目地址

#### [剑指 Offer 54. 二叉搜索树的第k大节点](https://leetcode-cn.com/problems/er-cha-sou-suo-shu-de-di-kda-jie-dian-lcof/)

#### 题目概述

![isBalanceTree](/images/kthLargestVal.png)

## 题目分析

为什么在中序遍历时候更改cnt值？大概是因**二叉搜索树的中序遍历是有序的**

1. 递归结束条件：当x节点为空时结束递归
2. 递归每层做什么：right -> mid -> left 依次访问，在中序遍历修改cnt值，当++cnt == k时记录val并结束遍历
3. 递归返回：无返回

## 题目解答

```java
int ans, cnt;

public int kthLargest(TreeNode root, int k) {

    if (root == null) {
        return 0;
    }

    kthLargestNode(root, k);
    System.out.println(ans);
    return ans;
}


public void kthLargestNode(TreeNode x, int k) {
    if (x == null) {
        return;
    }

    kthLargestNode(x.right, k);
    if (++cnt == k) {
        ans = (int) x.val;
        return;
    }
    kthLargestNode(x.left, k);

}
```

当然根据根据中序遍历的有序性，遍历所有k个大的数并返回最后一个元素即为第K个大的元素

```java
public int kthLargest2(TreeNode root, int k) {

    if (root == null) {
        return 0;
    }

    kthLargestNode2(root, k);
    Integer a = kthLargestValList.get(k - 1);
    System.out.println(a);
    return a;
}

/**
 * 二叉搜索树特点：中序遍历是有序的
 */
public void kthLargestNode2(TreeNode x, int k) {
    if (x == null) {
        return;
    }

    kthLargestNode2(x.right, k);
    kthLargestValList.add((Integer) x.val);
    if (kthLargestValList.size() == k) {
        return;
    }
    kthLargestNode2(x.left, k);
}

```