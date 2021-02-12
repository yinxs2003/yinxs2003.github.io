---
title: Flink-SingleOutputStreamOperator用法
date: 2020-11-30 10:40:30
toc: true
tags: 大数据 
categories: 
- 技术
---

`Flink DataStream`中`union`和`connect`都有一个共同的作用，就是将2个流或多个流合成一个流。但是两者的区别是：`union`连接的2个流的类型必须一致，`connect`连接的流可以不一致，但是可以统一处理。

具体看下面示例：

```java
public class ConnectOperator {

}
```

connect可以将2个不同类型的流同时用不同的逻辑处理好，形成一个流。

union是将2个同类型的流，合成一个，进行处理。

