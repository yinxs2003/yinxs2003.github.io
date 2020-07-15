---
title: beeline请求被提交到已经删除的hiveserver2节点
date: 2020-06-15 14:44:51
tags: 大数据
categories: 
- 技术
---

文章内容
<!--more-->

# 问题描述

在ambari集群上bigdata-client节点做hiveserver2自动拉起测试，增加了一个hiveserver2，然后再ambari删除该hiveserver2

然后出现请求被提交到已经删除的hiveserver2节点

![image-20200614163951660](/images/image-20200614163951660.png)

在zookeeper节点上执行zookeeper-cli.sh进入zookeeper命令行

执行删除无效的节点命令

```shell
delete /hiveserver2/serverUri=bigdata-client:10000;version=1.2.1000.2.6.5.1175-1;sequence=0000000010
```

过一段时间还是会出现上述错误，之后发现自动拉起服务没删除……