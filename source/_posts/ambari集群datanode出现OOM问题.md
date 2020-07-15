---
title: ambari集群datanode出现OOM问题
date: 2012-06-15 14:44:51
toc: true
tags: 大数据
categories: 
- 技术
---

文章内容
<!--more-->

# 问题描述

hadoop-worker004、hadoop-worker005有时候会断连接，ambari显示datanode dead，看日志里提示datanode OOM

# 解决方案

查看是由于hadoop-worker004、hadoop-worker005 CPU和IO负载压力太大导致的

经调查由于sql查询的数据在hadoop-worker004、hadoop-worker005上属于热点数据，且这两个节点数据比较多，需要做rebalance

# 其他

## 查看IO负载命令

```shell
utity占比，越接近100%，磁盘IO压力越大
iostat -dmx 1
```



