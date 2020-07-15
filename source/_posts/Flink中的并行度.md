---
title: Flink中的并行度
date: 2020-06-25 12:50:30
toc: true
tags: 大数据
categories: 
- 技术
---

# Parallel Execution（并行执行）

一个任务被切分成几个并行实例执行，且每个并行实例处理输入任务的一部分数据，并行度会导致乱序问题，任务的并行实力数称为并行性

可以从三个层面限制并行度

## Execution Environment Level

```java
env.setParallelism(3);
```

## Client Level

```java
Client client = new Client(jobManagerAddress, config, program.getUserCodeClassLoader());

// set the parallelism to 10 here
client.run(program, 10, true);
```

## System Level

在./conf/flink-conf.yaml中配置parallelism.default来设置并行度

## 提示

如果使用checkpoint的话需要设置一个最大并行度，避免从savepoint恢复时候导致性能问题