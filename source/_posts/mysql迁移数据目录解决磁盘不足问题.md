---
title: mysql迁移数据目录解决磁盘不足问题
date: 2020-06-18 10:40:30
toc: true
tags: Linux
categories: 
- 技术
---

先关闭mysql数据库

mv /var/lib/mysql /data/mysql_data

<!--more-->

创建目录

mkdir /data/mysql_data

chown mysql:mysql /data/mysql_data

做软连接

ln -s /data/mysql_data/mysql /var/lib/mysql