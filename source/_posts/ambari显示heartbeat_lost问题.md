---
title: python3下配置spark环境
date: 2020-07-10 12:00:30
toc: true
tags: Linux
categories: 
- 技术
---

在/etc/profile中增加如下内容

```shell
export JAVA_HOME=/usr/local/java/jdk1.8.0_191
export PYSPARK_PYTHON=/usr/python3/bin/python3
export PYSPARK_DRIVER_PYTHON=/usr/python3/bin/python3
export PYSPARK_SUBMIT_ARGS="--master local pyspark-shell"
export HADOOP_USER_CLASSPATH_FIRST=true
export PATH=$JAVA_HOME/bin:$PATH
export MAVEN_HOME=/usr/local/maven/apache-maven-3.5.4
export SPARK_HOME=/usr/hdp/2.6.5.0-292/spark2
export HADOOP_HOME=/usr/hdp/current/hadoop-client
export HIVE_HOME=/usr/hdp/current/hive-server2-hive2
export PATH=$SPARK_HOME/bin:$MAVEN_HOME/bin:$PATH
```

再执行

source /etc/profile