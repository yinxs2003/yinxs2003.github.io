---
title: Flink中的时间概念
date: 2020-06-25 12:50:30
tags: 大数据
---

# Event Time

事件时间指事件在设备上的发生时间，该时间通常在消息进入Flink之前发生，且该事件发生的时间戳可以从每条记录单独提取

# Ingestion Time(摄取时间)

摄取时间指的是消息进入flink的时间

# Processing Time

处理时间指的是执行某个任务时的系统时间，在消息进入flink且被window处理时发生



# 三种时间关系示意图 

![times_clocks](images/times_clocks.svg)

