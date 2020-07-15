---
title: spark-submit时候添加jars
date: 2020-06-03 10:40:30
toc: true
tags: 大数据
categories: 
- 技术
---

文章内容
<!--more-->

# spark-submit时单独添加jars

spark-submit --jars app-util-1.0.0.jar --class com.dankegongyu.dailyremain.DailyRemain --master yarn --deploy-mode client /data/bigdata/app-daily-remain-1.0.0.jar $yesterday