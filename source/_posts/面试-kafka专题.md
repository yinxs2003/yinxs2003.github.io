---
title: 面试-Kafka专题
date: 2020-07-11 12:44:51
tags: 面试
---

# 使用Kafka时会遇到的问题

1. Kafka速度很快的原因
2. 如何保证消息不丢失
3. Offset怎么保存
4. Consumer重复消费怎么处理
5. 如何保证消息的有序性
6. 数据倾斜怎么处理
7. 一个Topic分配多少个Partition合适以及修改Partition的限制有哪些

<!--more-->

# Kafka速度很快的原因

1. 磁盘的顺序读写
2. 使用操作系统的Page Cache，而不是jvm内存，这样能极大加快读写速度。避免Object对象比在Linux对象更大的消耗，避免jvm数据增多GC变慢的问题
3. 零拷贝，Page Cache 结合 sendfile 方法，避免拷贝到用户态内存操作，Kafka消费端的性能也大幅提升。这也是为什么有时候消费端在不断消费数据时，我们并没有看到磁盘io比较高，此刻正是操作系统缓存在提供数据。
4. 批量读写，批量压缩：把所有消息都变成一个批量文件，并且执行合理的批量压缩，减少网络IO消耗

# 如何保证消息不丢失

## 消息发布的可靠性

场景：Producer->Broker

1. 当Producer向Broker发消息时，一旦这条消息被commit，因为replication的存在，它就不会丢失
2. 但如果Producer发送数据给Broker后，遇到网路问题而造成通信中断，那Producer就无法判断该消息是否已经commit
3. 所以目前默认情况下，一条消息从Producer到Broker确保了At Least Once，可以通过设置Producer一部发送实现At Most Once

解决方案：

Producer可以生成主键，发生故障时密等的重试多次，这样就保证了消息发布的可靠性

## 消息接受的可靠性



# Kafka在分布式情况下如何保证消息的有序性

1. 同一个partition消息是有序的

2. Kafka 中发送1条消息的时候，可以指定(topic, partition, key) 3个参数

   指定partition参数，则发往同一个partition的消息是有序的

   指定key，具有同一个key的所有消息都会发送到一个partiton，也能够保证局部有序

# kafka怎么保证数据不丢失

# 如何保证Kafka最少一次消费以及精确消费

# ACK含义

