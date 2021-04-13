package com.code.note.test;

public class Test2 {
    public static void main(String[] args) {
        int[][] arr = new int[][]{
                {1, 3, 5, 7},
                {10, 11, 16, 20},
                {23, 30, 34, 50}
        };


        Test2 test = new Test2();

        int target = 3;

        int row = 0;
        int len1 = arr.length;

        boolean resultVal = false;

        while (row < len1) {
            int index = test.binarySearch(target, arr[row], 0, arr[0].length);
            if (index == -1) {
                row++;
            } else {
                resultVal = true;
                break;
            }
        }
        System.out.println(resultVal);
    }


    public int binarySearch(int target, int[] arr, int left, int right) {

        while (left < right) {
            int mid = (left + right) / 2;
            if (arr[mid] > target) {
                right = mid - 1;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else if (arr[mid] == target) {
                return mid;
            }
        }
        return -1;
    }

}
