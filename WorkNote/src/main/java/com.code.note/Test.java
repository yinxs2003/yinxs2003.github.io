package com.code.note;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class Test {
    public static void main(String[] args) {
        Queue<Integer> e = new LinkedList<>();
        e.offer(1);
        e.offer(3);
        e.offer(4);
        e.offer(5);
        e.offer(6);
        e.offer(7);
        e.offer(8);
        e.offer(9);

        log.info("{}",e);
        log.info("{}",e.poll());
        log.info("{}",e.poll());
        log.info("{}",e.poll());
        log.info("{}",e.poll());
        log.info("{}",e.poll());


    }
}
