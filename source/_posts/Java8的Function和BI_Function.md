---
title: Java8的Function和BI_Function
date: 2020-10-20 10:40:30
toc: true
tags: Java 
categories: 
- 技术
---

# 概念

 Function作为一个函数式接口，主要方法apply接受一个参数，返回一个值

# 举个例子

```java
package com.bd.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class Test {
    public static void main(String[] args) {
        Function<Integer, Integer> f1 = x -> {
            return x * x;
        };
        Function<Integer, Integer> f2 = x -> {
            return x + x;
        };
        log.info("{}", f1.compose(f2).apply(3));
        log.info("{}", f1.andThen(f2).apply(3));

    }
}
```

根据这个例子可以看出

f1.compose(f2)是先执行f2再执行f1函数，

f1.andThen(f2)是先执行f1再执行f2函数

