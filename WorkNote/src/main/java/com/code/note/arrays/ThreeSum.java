package com.code.note.arrays;

import com.code.note.sort.QuickSort;

import java.util.*;

public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        QuickSort.quickSort(nums, 0, nums.length - 1);

        Set<List<Integer>> arr = new HashSet<>();
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                int target = -(nums[i] + nums[j]);
                int index = binarySearch(nums, target, j+1, nums.length - 1);
                if (index != -1) {
                    addNewElement(arr, nums[i], nums[j], nums[index]);
                }
            }
        }

        return new ArrayList<>(arr);
    }

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

    private void addNewElement(Set<List<Integer>> arr, int num, int num1, int num2) {

        List<Integer> a = new ArrayList<>();
        a.add(num);
        a.add(num1);
        a.add(num2);
        arr.add(a);
    }

    public static void main(String[] args) {
        int[] nums = {-1,0,1,2,-1,-4};
        ThreeSum threeSum = new ThreeSum();
        List<List<Integer>> arr = threeSum.threeSum(nums);
        System.out.println(arr);
    }
}
