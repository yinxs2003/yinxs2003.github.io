package com.code.note.sort;

import java.util.Arrays;

public class QuickSort {
    public static void quickSort(int[] nums, int left, int right) {

        if (left >= right) {
            return;
        }

        int pIndex = partition(nums, left, right);
        quickSort(nums, left, pIndex - 1);
        quickSort(nums, pIndex + 1, right);
    }

    private static int partition(int[] nums, int left, int right) {

        int pivot = left;
        int pIndex = pivot + 1;

        for (int i = pIndex; i <= right; i++) {
            if (nums[i] < nums[pivot]) {
                swap(nums, i, pIndex);
                pIndex++;
            }
        }
        swap(nums, pivot, pIndex - 1);

        return pIndex - 1;

    }

    private static void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    public static void main(String[] args) {
        int[] arr = {6, 5, 4, 5, 1, 2, 3};
        QuickSort.quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
