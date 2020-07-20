---
title: Centos7安装python3
date: 2020-07-20 12:00:30
toc: true
tags: Linux
categories: 
- 技术
---

## 下载python3安装包

wget -c https://www.python.org/ftp/python/3.7.3/Python-3.7.3.tgz

## 安装linux依赖

yum install libffi-devel openssl openssl-devel sqlite-devel bzip2-devel -y

在源码安装文件中编辑文件Modules/Setup.dist

## 安装openssl

在源码安装文件中编辑文件Modules/Setup.dist

去掉如下注释

```python
#SSL=/usr/local/ssl
_ssl _ssl.c \
 -DUSE_SSL -I$(SSL)/include -I$(SSL)/include/openssl \
 -L$(SSL)/lib -lssl -lcrypto
```

## 编译安装

mkdir /usr/python3

cd Python-3.7.3

./configure --prefix=/usr/python3 --enable-loadable-sqlite-extensions && make && make install

ln -s /usr/python3/bin/python3 /usr/bin/python3

ln -s /usr/python3/bin/pip3 /usr/bin/pip3