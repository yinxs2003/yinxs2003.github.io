package com.code.note.binarysearch;

import java.util.*;

public class Test {

    public List<Integer> binarySearch(int[] arr, int target, int N) {
        int left = 0;
        int right = arr.length - 1;
        int mid = (left + right) / 2;
        int index = 0;
        while (left < right) {
            mid = (left + right) / 2;
            if (arr[mid] > target) {
                right = mid - 1;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else if (arr[mid] == target) {
                index = mid;
                break;
            }
        } // logn
        index = mid;

        if (arr[index] >= target) {
            left = index;
            right = index + 1;
        } else if (arr[index] < target) {
            left = index - 1;
            right = index;
        }

        List<DistanceTuple> arrList1 = new ArrayList<>();

        int t1 = left;
        int N1 = N;


        while (t1 > 0 && N1 > 0) {
            DistanceTuple tmp = new DistanceTuple(Math.abs(target - arr[t1]), arr[t1]);
            arrList1.add(tmp);
            t1--;
            N1--;
        }

        int t2 = right;
        int N2 = N;


        while (t2 < arr.length && N2 > 0) {
            DistanceTuple tmp = new DistanceTuple(Math.abs(target - arr[t2]), arr[t2]);
            arrList1.add(tmp);
            t2++;
            N2--;
        }

        Collections.sort(arrList1);

        System.out.println(arrList1);

        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < N; i++) {

            resultList.add(arrList1.get(i).targetVal);
        }
        return resultList;
    }

    class DistanceTuple implements Comparable<DistanceTuple> {
        int distance;
        int targetVal;

        public DistanceTuple(int distance, int targetVal) {
            this.distance = distance;
            this.targetVal = targetVal;
        }

        @Override
        public int compareTo(DistanceTuple o) {
            return this.distance - o.distance;
        }

        @Override
        public String toString() {
            return "DistanceTuple{" +
                    "distance=" + distance +
                    ", targetVal=" + targetVal +
                    '}';
        }
    }


    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 8, 11, 12, 14, 16, 20, 26, 27, 28};
        Test t = new Test();
        List<Integer> list = t.binarySearch(arr, 14, 3);
        System.out.println(list);
    }
}
