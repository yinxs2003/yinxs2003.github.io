---
title: Presto导出到csv
date: 2020-06-03 10:40:30
tags: 大数据
---

文章内容
<!--more-->

nohup presto --server localhost:9090 --catalog hive_danke --schema default -f /tmp/tmp.sql --output-format CSV &> /data/xiongyan_yanzheng.csv &

