---
title: 算法-二叉树的最近公共祖先
date: 2021-02-19
toc: true
tags: 算法
categories: 
- 技术
---

## 题目概述

输入字符串转成double数字

## 题目分析

1. 字符串是否为空
2. 字符串是否包含非数字
3. 字符串是否包含正负号
4. 是否超过double最大值

## 题目解答

```java
package com.code.note.test;

public class Solution {

    public Double parseDouble(String n) {

        int isNegative = 1;

        String numStr = n.trim();
        if (numStr == null) return 0D;
        if (numStr.length() > 0 && numStr.charAt(0) == '-') {
            isNegative = -1;
            numStr = numStr.substring(1);
        } else if (numStr.charAt(0) == '+') {
            numStr = numStr.substring(1);
        }

        if (!numStr.contains(".")) {
            return (double) getHighPosNum(n);
        }

        String[] arr = numStr.split("\\.");

        double sumHigh = getHighPosNum(arr[0]);

        double sumLow = getLowPosNum(arr[1]);

        return isNegative * (sumLow + sumHigh);
    }

    private long getHighPosNum(String highStr1) {
        long sumHigh = 0;
        String highStr = highStr1;
        char[] highChars = highStr.toCharArray();
        int[] highIntNums = new int[highChars.length];

        for (int i = 0; i < highIntNums.length; i++) {
            sumHigh += Math.pow(10, highIntNums.length - 1 - i) * isDigit(highChars[i]);
        }
        return sumHigh;
    }

    private int isDigit(char highChar) {
        if (!Character.isDigit(highChar)) {
            throw new NumberFormatException("非法数字");
        }
        return (int) highChar - (int) '0';
    }

    private double getLowPosNum(String lowStr1) {
        double sumLow = 0D;
        String lowStr = lowStr1;
        char[] lowChars = lowStr.toCharArray();
        int[] lowIntNums = new int[lowChars.length];

        for (int i = 0; i < lowIntNums.length; i++) {
            sumLow += Math.pow(10, -1 * (i + 1)) * (isDigit(lowChars[i]));
        }
        return sumLow;
    }

    public static void main(String[] args) {

        Solution s = new Solution();
        double a = s.parseDouble("-1.456");
        System.out.println(a);
    }
}

```

