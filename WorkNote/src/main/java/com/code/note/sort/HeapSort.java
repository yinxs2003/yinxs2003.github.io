package com.code.note.sort;

import com.code.note.tree.BinaryHeap;

import java.util.Arrays;

public class HeapSort {

    public void buildBigHeap(int[] arr, int parent, int len) {
        int child = parent * 2 + 1;
        while (child < len) {
            if (child + 1 < len && arr[child] < arr[child + 1]) {
                child++;
            }

            if (arr[child] < arr[parent]) {
                break;
            }

            swap(arr, child, parent);

            parent = parent * 2 + 1;
            child = parent * 2 + 1;
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

    public void heapSort(int[] arr) {
        // 构建大顶堆
        for (int i = 0; i < arr.length / 2; i++) {
            buildBigHeap(arr, 0, arr.length);
        }

        for (int i = 0; i < arr.length; i++) {
            swap(arr, 0, arr.length - i - 1);
            buildBigHeap(arr, 0, arr.length - 1 - i);
        }
        print(arr);

    }

    public static void main(String[] args) {

        HeapSort s = new HeapSort();

        int[] arr = new int[]{1, 3, 41, 55, 2};

        s.print(arr);

        s.heapSort(arr);


        System.out.println(Arrays.toString(arr));
    }
}
