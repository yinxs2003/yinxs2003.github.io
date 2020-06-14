---
title: spark-submit时候添加jars
date: 2020-06-03 10:40:30
tags: 大数据
---

文章内容
<!--more-->

spark-submit --jars app-util-1.0.0.jar --class com.dankegongyu.dailyremain.DailyRemain --master yarn --deploy-mode client /data/bigdata/app-daily-remain-1.0.0.jar $yesterday