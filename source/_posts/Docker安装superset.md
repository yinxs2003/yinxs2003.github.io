---
title: Docker安装superset
date: 2020-06-03 10:40:30
toc: true
tags: 大数据
categories: 
- 技术
---

文章内容
<!--more-->

# 挂载目录到本地

mkdir /Users/yangaipeng/Desktop/incubator-superset/docker_superset/superset

# 启动docker superset

docker run -d -p 8088:8088 -v /Users/yangaipeng/Desktop/incubator-superset/docker_superset/superset:/home/superset --name superset2 superset-release-tag

# 增加配置文件

将配置文件superset_config.py放到/Users/yangaipeng/Desktop/incubator-superset/docker_superset/superset目录下

在superset_config.py将mysql地址配置为host.docker.internal，表示让docker中的superset访问本机数据库

# 设置用户名和密码

docker exec -it 2326fa0a7d8b fabmanager create-admin --app superset

# 初始化数据库

docker exec -it 2326fa0a7d8b superset db upgrade

# superset初始化

docker exec -it 2326fa0a7d8b superset init

# 开启superset服务

docker exec -it 2326fa0a7d8b superset runserver

# 使用root用户运行docker bash

docker --user root -it exec 1b9e081ef94f bash
