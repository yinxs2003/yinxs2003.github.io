---
title: 算法-数组中的第K个最大元素
date: 2021-04-08
toc: true
tags: 算法
categories: 
- 技术
---

### 题目描述

```java
在未排序的数组中找到第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。

示例 1:

输入: [3,2,1,5,6,4] 和 k = 2
输出: 5
示例 2:

输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
输出: 4
说明:

你可以假设 k 总是有效的，且 1 ≤ k ≤ 数组的长度。
```

该题可以使用堆排序进行实现，时间复杂度为nlogk

### 如何实现

```java
package com.code.note.test;

import com.code.note.tree.BinaryHeap;

public class Solution2 {

    public void buildBigHeap(int[] arr, int parent, int len) {
        int child = parent * 2 + 1;
        while (child < len) {
            if (child + 1 < len && arr[child] < arr[child + 1]) {
                child++;
            }

            if (arr[child] <= arr[parent]) {
                break;
            }

            swap(arr, child, parent);

            parent = child;
            child = 2 * child + 1;
        }
    }

    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public void print(int[] arr) {
        BinaryHeap b = new BinaryHeap();
        b.insertLevelOrder(arr);
        b.print();
    }

    public int findKthLargest(int[] arr, int k) {

        // 构建大顶堆
        for (int i = arr.length / 2; i >= 0; i--) {
            buildBigHeap(arr, i, arr.length);
        }

        print(arr);

        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, 0, i);
            buildBigHeap(arr, 0, i);
        }
        return arr[arr.length - k];
    }

    public static void main(String[] args) {

        Solution2 s = new Solution2();

        int[] arr = new int[]{2, 1,4,123};

        s.print(arr);

        int a = s.findKthLargest(arr, 2);


        System.out.println(a);
    }
}
```

