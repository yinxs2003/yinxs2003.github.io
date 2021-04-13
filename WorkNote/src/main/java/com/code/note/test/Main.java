package com.code.note.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public void quickSort(int[] arr, int left, int right) {

        if (left >= right) return;

        int p = partition(arr, left, right);
        quickSort(arr, left, p - 1);
        quickSort(arr, p + 1, right);
    }

    //    nlogn
    private int partition(int[] arr, int left, int right) {
        int l = left;
        int r = right;
        while (l < r) {
            while (arr[l] < arr[left] && l < r) {
                l++;
            }

            while (arr[r] > arr[left] && l < r) {
                r--;
            }

            swap(arr, l, r);
        }
        swap(arr, l, r);
        return l;
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        Main m = new Main();
        int[] arr = {6, 4, 5, 3, 2, 1};
        m.quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));

    }
}