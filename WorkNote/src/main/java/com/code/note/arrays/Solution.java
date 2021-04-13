package com.code.note.arrays;

public class Solution {
    public double sqrt(int target, double l, double r) {
        double mid = (r + l) * 1.0 / 2;

        if(r == 1){
            return 1;
        }

        if (l >= r) {
            return (int) mid;
        }

        if (Math.round(mid * mid) == target) {
            return mid;
        } else if (mid * mid > target) {
            r = mid;
        } else {
            l = mid;
        }
        return sqrt(target, l, r);
    }

    public int mySqrt(int x) {
        double a = sqrt(x, 0, x);
        return (int)Math.round( a);

    }

    public static void main(String[] args) {
        Solution s = new Solution();
        int a = s.mySqrt(8);
        System.out.println(a);
    }
}

