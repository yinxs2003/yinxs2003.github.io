---
title: Linux-Es排除节点
date: 2021-04-08
toc: true
tags: Linux
categories: 
- 技术
---

### 题目描述

Es集群中有一个节点性能很差，会导致Es整体查询变慢，当该物理节点被关闭但存在部分分片没有分配到其他节点上。

### 解决方案

因为配置文件中被关闭的机器ip是存在的，Es存在一个探活过程，所以这些分片没有被自动迁移到其他节点。

可以通过Exclude该机器的IP来动态删除该机器，删除10.1.1.1节点命令：

```bash
curl -XPUT http://10.1.1.10:9210/_cluster/settings -d '{
"transient" :{
"cluster.routing.allocation.exclude._ip" : "10.1.1.1"
}
}'
```

