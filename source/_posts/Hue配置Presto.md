---
title: Hue配置Presto
date: 2020-06-03 10:40:30
tags: 大数据
categories: 
- 技术
---

文章内容
<!--more-->

# python3安装pyhive

进入hue安装目录/usr/local/hue/hue-4.3.0/

./build/env/bin/pip install pyhive

在/usr/local/hue/hue-4.3.0/desktop目录编辑

```
vim desktop/conf/hue.ini
[[[presto]]]
      name = Presto
      interface=sqlalchemy
      options='{"url": "presto://172.22.222.89:9090/hive_danke/default"}'

```

# 重启hue

kill pid 

/usr/local/hue/hue-4.3.0/build/env/bin/python2.7 /usr/local/hue/hue-4.3.0/build/env/bin/hue runcherrypyserver