---
title: Java线程池
date: 2020-06-03 10:40:30
tags: Java
---

文章内容

<!--more-->

# TheadPoolExecutor

## 队列满时候的丢弃策略

DiscardPolicy：不处理，直接丢弃掉；
AbortPolicy：直接抛出 RejectedExecutionException 异常，这是默认的拒绝策略；
DiscardOldestPolicy：丢弃最老的任务，并执行当前任务；
CallerRunsPolicy：由调用线程本身运行任务，以减缓提交速度，会阻塞主线程；