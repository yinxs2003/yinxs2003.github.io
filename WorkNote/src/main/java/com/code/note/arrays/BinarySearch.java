package com.code.note.arrays;

public class BinarySearch {
    public static int binarySearch(int[] arr, int target, int low, int high) {
        int l = low;
        int h = high;

        while (l <= h) {
            int mid = (h + l) / 2;
            if (arr[mid] > target) {
                h = mid - 1;

            } else if (arr[mid] < target) {
                l = mid + 1;
            } else {
                return mid;
            }

        }
        return -1;
    }

    public static void main(String[] args) {
        int[] a1 = {-4, -1, -1, 0, 1, 2};
        int index = BinarySearch.binarySearch(a1, 1, 0, a1.length - 1);
        System.out.println(index);
    }
}
