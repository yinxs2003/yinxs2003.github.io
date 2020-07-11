---
title: 手动编译安装flink，适配ambari hdp版本hadoop
date: 2020-06-03 10:40:30
tags: 大数据
---

文章内容
<!--more-->

# 找到ambari hadoop版本

在hdp下载目录找到当前hadoop版本

http://repo.hortonworks.com/content/repositories/releases/org/apache/hadoop/hadoop-common/

前面是hadoop版本，后面是hdp版本



# clone flink

clone flink源码

git clone https://github.com/apache/flink.git

切换分支



# 手动编译

在工程目录执行 git checkout release-1.6.4-rc1

Flink编译安装

mvn clean install -DskipTests -Drat.skip=true -Pvendor-repos -Dhadoop.version=2.7.3.2.6.5.0-292

参考：

https://www.bbsmax.com/A/B0zqV4OnJv/