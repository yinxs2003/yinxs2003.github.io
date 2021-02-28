package com.code.note.arrays;

import java.util.ArrayList;
import java.util.List;

public class ThreeSum {



    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> arr = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0 && notExist(arr, nums[i], nums[j], nums[k])) {
                        addNewElement(arr, nums[i], nums[j], nums[k]);
                    }
                }
            }
        }

        return arr;
    }

    public boolean notExist(List<List<Integer>> arr, int numI, int numJ, int numK) {
        for (List<Integer> existList : arr) {
            int a = existList.get(0);
            int b = existList.get(1);
            int c = existList.get(2);
            boolean a1 = (a == numI && b == numJ && c == numK);
            boolean a2 = (a == numI && b == numK && c == numJ);

            boolean b1 = (a == numJ && b == numK && c == numI);
            boolean b2 = (a == numJ && b == numI && c == numK);

            boolean c1 = (a == numK && b == numI && c == numJ);
            boolean c2 = (a == numK && b == numJ && c == numI);

            if (a1 || a2 || b1 || b2 || c1 || c2) {
                return false;
            }

        }

        return true;
    }

    private void addNewElement(List<List<Integer>> arr, int num, int num1, int num2) {
        List<Integer> a = new ArrayList<>();
        a.add(num);
        a.add(num1);
        a.add(num2);
        arr.add(a);
    }

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        ThreeSum threeSum = new ThreeSum();
        List<List<Integer>> arr = threeSum.threeSum(nums);
        System.out.println(arr);
    }
}
