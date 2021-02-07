package com.code.note.recursion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Factorial {
    public static int cal(int n) {
        if (n == 0) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        return n * cal(n - 1);
    }

    public static void main(String[] args) {
        log.info("{}", cal(5));
    }
}
