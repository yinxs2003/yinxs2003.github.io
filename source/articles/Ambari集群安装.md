---
title: Ambari集群安装
date: 2020-06-03 10:40:30
tags: 大数据
---

文章内容
<!--more-->



## 目前的机器

172.22.222.109



## 安装ambari-server

在/etc/yum.repos.d/ambari.repo中新增ambari源

 

```
cat /etc/yum.repos.d/ambari.repo
#VERSION_NUMBER=2.6.2.2-1
[ambari-2.6.2.2]
name=ambari Version - ambari-2.6.2.2
baseurl=http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/2.6.2.2
gpgcheck=1
gpgkey=http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/2.6.2.2/RPM-GPG-KEY/RPM-GPG-KEY-Jenkins
enabled=1
priority=1
```

执行安装命令

yum install install ambari-server

## 安装MariaDB 10.1 

```
cat /etc/yum.repos.d/Mariadb.repo
[mariadb]
name = MariaDB
baseurl = https://mirrors.tuna.tsinghua.edu.cn/mariadb/yum/10.1/centos7-amd64
gpgkey = https://mirrors.tuna.tsinghua.edu.cn/mariadb/yum//RPM-GPG-KEY-MariaDB
gpgcheck=1

yum install mariadb-server mariadb mariadb-client mariadb-devel
```

## 部署的目录

/var/lib/ambari-server



## web访问地址

[http://172.22.222.109:9090](http://172.22.222.109:9090/)



## 配置文件的位置

/etc/ambari-server/conf

## 管理脚本

service ambari-server status

service ambari-server restart

service ambari-server stop

service ambari-server start

## 添加Client机器报SSL错误问题

编辑要增加的客户端机器/etc/ambari-agent/conf/ambari-agent.ini

在security标签增加

force_https_protocol=PROTOCOL_TLSv1_2

## 安装时报删除服务失败问题

查看/var/log/ambari-server/ambari-server.log日志报

*Caught AmbariException when modifying a resource*
*org.apache.ambari.server.AmbariException: Could not delete cluster, clusterName=localvps*

*01 Apr 2020 12:26:06,836 WARN [ambari-client-thread-34] ServiceComponentImpl:492 - Found non removable hostcomponent when trying to delete service component, clusterName=localvps, serviceName=HIVE, componentName=MYSQL_SERVER, state=STARTED, hostname=localvps*
*01 Apr 2020 12:26:06,836 WARN [ambari-client-thread-34] ServiceImpl:509 - Found non removable component when trying to delete service, clusterName=localvps, serviceName=HIVE, componentName=MYSQL_SERVER*



这个问题是由于安装hive时候选择New Database，之后又选择exist Database导致的，删除mysql数据库后重新安装MariaDB 10.1之后该问题消失