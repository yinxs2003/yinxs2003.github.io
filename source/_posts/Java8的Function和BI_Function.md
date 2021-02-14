---
title: Java8的Function和BI_Function
date: 2020-10-20 10:40:30
toc: true
tags: 技术Java
categories: 
- 技术
---

# Function函数

## 概念

 Function作为一个函数式接口，主要方法apply接受一个参数，返回一个值

## 举个例子

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
      
        
        // 表示f1参数由f2函数构成，即(x + x) * (x + x)
        //（3+3）*（3+3）= 36
        log.info("{}", f1.compose(f2).apply(3));
        
        // 表示先执行f1函数，再执行f2函数
        //（3*3）+（3*3）=18
        log.info("{}", f1.andThen(f2).apply(3));

    }
}
```

## Function接口源码如下

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
   
    // 表示f1参数由f2函数构成，即(x + x) * (x + x)
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    // 表示先执行f1函数，再执行f2函数
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }
}
```



# BIFunction函数

BiFunction也是一个函数式接口，和Function接口不同的是，它在接口中声明了3个泛型，其中前两个作为方法参数类型，最后一个作为返回类型

```java
package com.bd.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public class Test {
    public static void main(String[] args) {
        BiFunction<String, String, String> f1 = (x, y) -> "world " + x + y;
        Function<String, String> f2 = (x) -> "hello " + x;

        // world lisi
        log.info(f1.apply("li", "si"));
        
        // hello + world lisi
        log.info(f1.andThen(f2).apply("li", "si"));
    }
}
```

根据这个例子可以看出

f1.andThen(f2)是**先计算f1且将结果作为参数传入f2中**

BiFunction接口源码如下，可以看出由after（f2参数）调用apply方法执行f1的apply方法，即将f1计算结果传入f2中

```java
@FunctionalInterface
public interface BiFunction<T, U, R> {

    R apply(T t, U u);

    default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> after.apply(apply(t, u));
    }
}
```

