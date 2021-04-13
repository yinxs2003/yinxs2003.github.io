package com.code.note.test;

import com.code.note.tree.HeapNode;

import java.util.PriorityQueue;

public class Solution2 {
    PriorityQueue p;
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
        HeapNode b = new HeapNode();
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
