package com.code.note.test;

import com.code.note.tree.TreeNode;
import com.code.note.tree.UnOrderBTree;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

//    public TreeNode constructMaximumBinaryTree(int[] nums) {
//        int bigIndex = findMaxIndex(nums);
//    }
//
//    public int findMaxIndex(int[] arr) {
//        int bigIndex = arr[0];
//        for (int i = 0; i < arr.length; i++) {
//            if (arr[i] > arr[bigIndex]) {
//                bigIndex = i;
//            }
//        }
//        return bigIndex;
//    }

    public static void main(String[] args) {

//        Solution s = new Solution();
//        Integer[] arr1 = {3, 2, 1, 6, 0, 5};

//        System.out.println(Arrays.toString(arr1));
        List<String> aList = Lists.newArrayList("1", "2", "3", "4", "5", "6");
        String a = String.join(".", aList);
        System.out.println(a);
    }
}
