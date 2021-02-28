package com.code.note.arrays;

import java.util.Arrays;

public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        int[] arr = new int[2];
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    arr[0] = i;
                    arr[1] = j;
                    break;
                }
            }
        }

        return arr;
    }

    public static void main(String[] args) {
        TwoSum t = new TwoSum();
        int[] arr = t.twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(arr));
    }
}
