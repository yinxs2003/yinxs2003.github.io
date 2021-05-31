---
title: Linux-vim正则查找
date: 2021-03-05
toc: true
tags: Linux
categories: 
- 技术
---

使用vim查询**spend=14435**的命令是,

/spend=[1-9]\\{5,5\\}

其中[1-9]表示任意一个1～9的数字

\\{5,5\\}上面的数字表示出现了5次，即匹配14435