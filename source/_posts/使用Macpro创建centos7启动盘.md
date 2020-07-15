---
title: 使用Macpro创建centos7启动盘
date: 2020-06-03 10:40:30
toc: true
tags: Linux
categories: 
- 技术
---

文章内容
<!--more-->



# Macpro 查看新增优盘

diskutil list

umount u盘

diskutil unmountDisk /dev/disk2

# Macpro dd命令写入

sudo dd if=/Users/yxs1112003/Downloads/CentOS-7-x86_64-DVD-1503-01.iso of=/dev/disk2 bs=4m

centos7 显示进度条的dd命令

dd if=./centos7.1.iso of=/dev/sdb1 status=progress