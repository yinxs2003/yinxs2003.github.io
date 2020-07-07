---
title: Flink提交任务
date: 2020-06-26 11:20:30
tags: 大数据

---

Flink以YarnCluster模式提交任务，且指定任务名和队列

flink run -m yarn-cluster -ynm PROD-fink-data-gather  --yarnqueue CClient /home/cclient/danke-flink-data-gather-prod.jar

<!--more-->

