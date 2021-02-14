---
title: 使用arthas分析presto宕机原因
date: 2020-06-20 10:40:30
toc: true
tags: 技术Java
categories: 
- 技术
---

 

# 问题描述

Presto半夜总是宕机

# 解决思路

使用arthas分析jvm使用情况

<!--more-->

## 下载arthas

 curl -O https://alibaba.github.io/arthas/arthas-boot.jar 

## 启动

```
java -jar arthas-boot.jar
```

## 命令

### thread命令查看最占CPU的线程

```
#展示当前最忙的前N个线程并打印堆栈
thread -n 3 
```
