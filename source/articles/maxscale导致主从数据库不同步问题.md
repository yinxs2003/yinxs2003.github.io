---
title: maxscale导致主从数据库不同步问题
date: 2016-06-13
tags: 数据库
---

文章内容
<!--more-->
### 问题描述
数据库集群使用的是多主机同步模式（mariadb galera cluster），但使用了maxscale之后查询的结果导致数据不同步

### 复现步骤
使用hibernate插入后立即查询（只要插入和查询离得足够近），会使slave和master机器查询出的结果不一致

### 问题定位
maxscale从slave库查询导致和主机不一致

### 解决方案
使用slavelag插件


### 编译安装maxscale

准备
``` bash 
cd  ~
mkdir maxscale && cd maxscale
git clone https://github.com/mariadb-corporation/MaxScale.git
mkdir build && cd build
```

编译
``` bash
cmake ../MaxScale -DBUILD_SLAVELAG=Y 
或者指定安装目录
cmake ../MaxScale -DBUILD_SLAVELAG=Y -DCMAKE_INSTALL_PREFIX=/usr
```

安装
``` bash
sudo make
sudo make install
来自 <https://groups.google.com/forum/#!topic/maxscale/fBF82MyRBVs> 
```

### 将slavelag插件添加到线上环境
在本地61环境编译安装maxscale2.0，由于2.0版本改动比较大，且线上环境使用的是1.4.3版本的maxscale，所以使用2.0源码编译，然后将slavelag.so拷贝到线上版本为1.4.3环境中使用

在maxscale2.0（62云主机）运行
locate slavelag

找到两个so文件：
/usr/lib64/maxscale/libslavelag.so
/usr/lib64/maxscale/libslavelag.so.1.1.0

将这两个文件，拷贝至1.4.3环境中，运行如下命令
scp /usr/lib64/maxscale/libslavelag.* 172.27.12.61:/tmp
sudo cp /tmp/libslavelag.* /usr/lib64/maxscale/

修改配置文件/etc/maxscale.cnf，增加slavelag filter，表示插入后2s内查询都会过滤到master机器执行

``` bash
[slavelag]
type=filter
module=slavelag
time=2

[Read-Write Service]
type=service
router=readwritesplit
enable_root_user=1
servers=server1, server2, server3
user=root
passwd=9F3E0D55B92626FD1CD9912F17091F12
max_slave_connections=100%
filters=slavelag
```

