---
title: Hadoop集群安装
date: 2016-10-29
tags: 大数据
---

文章内容
<!--more-->
###环境概述
两个namenode： 172.21.0.23，172.21.0.24
连个节点作为datanode：172.21.0.25,172.21.0.26
### 配置ssh免登陆
在172.21.0.23创建sshkey的压缩包
```bash
cd ~/.ssh/                     
ssh-keygen -t rsa              
cat ./id_rsa.pub >> ./authorized_keys  # 加入授权
tar -zcf auth.tar.gz authorized_keys id_rsa id_rsa.pub
```
将auth.tar.gz复制到其他三个节点，替换原有的key文件,在namenode节点分别执行ssh，免密码登录
```bash
ssh 172.21.0.25
ssh 172.21.0.26
```
### 安装OracleJDK
下载并安装jdk1.7
yum install jdk1.7.0_79.rpm
配置环境变量
vim /etc/profile
```
#JAVA HOME
export JAVA_HOME=/usr/java/jdk1.7.0_79
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```
source /etc/profile
### 安装zookeeper
在172.21.0.23节点
在官网下载稳定版,解压到zookeeper文件夹
将zookeeper/conf/zoo_sample.cfg改成zoo.cfg，修改文件中配置如下
```bash
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/home/usr/local/zookeeper/data
dataLogDir=/home/usr/local/zookeeper/datalog
clientPort=2181
server.1=172.21.0.23:2888:3888
server.2=172.21.0.24:2888:3888 
server.3=172.21.0.25:2888:3888
```
创建文件/home/usr/local/zookeeper/data/myid，内容为1，表示对应上述172.21.0.23
将数据拷贝的其他节点，分别启动zookeeper执行
zookeeper：sh zkServer.sh start

添加zookeeper的自启动,vim /etc/init.d/zookeeper
```bash
#!/bin/bash
#chkconfig:2345 20 90
#description:zookeeper
#processname:zookeeper
case $1 in
          start) su root  /home/usr/local/zookeeper/bin/zkServer.sh start;;
          stop) su root   /home/usr/local/zookeeper/bin/zkServer.sh stop;;
          status) su root  /home/usr/local/zookeeper/bin/zkServer.sh status;;
          restart) su root /home/usr/local/zookeeper/bin/zkServer.sh restart;;
          *)  echo "require start|stop|status|restart";;
esac
```
添加执行权限
```bash
chmod +x /etc/init.d/zookeeper
```
添加zookeeper的自启动
```bash
chkconfig --add zookeeper
chkconfig zookeeper on
```
### 安装Hadoop
下载Hadoop并解压缩
``` bash 
wget http://mirror.bit.edu.cn/apache/hadoop/common/hadoop-2.7.1/hadoop-2.7.1.tar.gz

tar -zxf hadoop-2.7.1.tar.gz -C /home/usr/local
cd /home/usr/local
mv hadoop-2.7.1 hadoop
```
验证hadoop是否可用
```bash
cd /home/usr/local/hadoop
./bin/hadoop version
```
### 配置hadoop集群
编辑core-site.xml
``` xml
<configuration>
        <property>
                <name>fs.defaultFS</name>
                <!-- <value>hdfs://172.21.0.23:9000</value> -->
                <value>hdfs://172.21.0.23:8020</value>
        </property>
        <property>
                <name>hadoop.tmp.dir</name>
                <value>file:/home/usr/local/hadoop/tmp</value>
                <description>Abase for other temporary directories.</description>
        </property>
        <property>

        <name>io.file.buffer.size</name>
                <value>131702</value>
        </property>
        <!--后添加-->
        <property>
               <name>ha.zookeeper.quorum</name>
               <value>172.21.0.23:2181,172.21.0.24:2181,172.21.0.25:2181,172.21.0.26:2181</value>
        </property>
        <property>
                <name>ha.zookeeper.parent-znode</name>
                <value>/hadoop-ha</value>
        </property>
        <property>
                <name>hadoop.proxyuser.hadoop.hosts</name>
                <value>*</value>
        </property>
        <property>
                <name>hadoop.proxyuser.hadoop.groups</name>
                <value>*</value>
        </property>
</configuration>
```
修改hdfs-site.xml
```xml
<configuration>
        <property>
                <name>dfs.namenode.secondary.http-address</name>
                <value>172.21.0.24:50090</value><!--文档配置的为9001端口 -->
        </property>
        <property>
                <name>dfs.replication</name>
                <value>2</value>
        </property>
        <property>
                <name>dfs.namenode.name.dir</name>
                <value>file:/home/usr/local/hadoop/tmp/dfs/name</value>
        </property>
        <property>
                <name>dfs.datanode.data.dir</name>
                <value>file:/home/usr/local/hadoop/tmp/dfs/data</value>
        </property>

        <!-- 开启list命令等-->
        <property>
                <name>dfs.webhdfs.enabled</name>
                <value>true</value>
        </property>

        <property>
                  <name>dfs.namenode.datanode.registration.ip-hostname-check</name>
                  <value>false</value>
        </property>
        <property>
                  <name>dfs.datanode.http.address</name>
                  <value>0.0.0.0:50075</value>
        </property>

        <!--从这里开始是新增内容，作为HA配置 -->
        <property>
                  <name>dfs.ha.automatic-failover.enabled</name>
                  <value>true</value>
        </property>
         <property>
                <name>dfs.nameservices</name>
                <value>hadoop-ha</value>
        </property>
        <property>
                <name>dfs.ha.namenodes.hadoop-ha</name>
                <value>nn1,nn2</value>
        </property>
        <property>
                <name>dfs.namenode.rpc-address.hadoop-ha.nn1</name>
                <value>172.21.0.23:8020</value>
        </property>
        <property>
                <name>dfs.namenode.rpc-address.hadoop-ha.nn2</name>
                <value>172.21.0.24:8020</value>
        </property>
        <property>
                <name>dfs.namenode.http-address.hadoop-ha.nn1</name>
                <value>172.21.0.23:50070</value>
        </property>
        <property>
                <name>dfs.namenode.http-address.hadoop-ha.nn2</name>
                <value>172.21.0.24:50070</value>
        </property>

        <property>
                <name>dfs.namenode.shared.edits.dir</name>
                <value>qjournal://172.21.0.23:8485;172.21.0.24:8485;172.21.0.25:8485/hadoop-ha</value>
        </property>
        <property>
                <name>dfs.journalnode.edits.dir</name>
                <!-- <value>/home/hadoop/hdfs/journal</value> -->
                <value>/home/usr/local/hadoop/journal</value>
        </property>
        <property>
                <name>dfs.journalnode.rpc-address</name>
                <value>0.0.0.0:8485</value>
        </property>
        <property>
                <name>dfs.journalnode.http-address</name>
                <value>0.0.0.0:8480</value>
        </property>
        <property>
                <name>dfs.client.failover.proxy.provider.hadoop-ha</name>
                <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
        </property>
        <property>
                <name>dfs.ha.fencing.methods</name>
                <value>sshfence</value>
        </property>
        <property>
                <name>dfs.ha.fencing.methods</name>
                <value>shell(/bin/true)</value>
        </property>
        <property>
                <name>dfs.ha.fencing.ssh.connect-timeout</name>
                <value>10000</value>
        </property>
        <property>
                <name>dfs.ha.fencing.ssh.private-key-files</name>
                <value>/root/.ssh/id_rsa</value>
        </property>
</configuration>
```
修改mapred-site.xml
```xml
<configuration>
        <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
        </property>
        <property>
                <name>mapreduce.jobhistory.address</name>
                <!-- <value>oasis-hdfs1:10020</value> -->
                <value>172.21.0.23:10020</value>
        </property>
        <property>
                <name>mapreduce.jobhistory.webapp.address</name>
                <!--<value>oasis-hdfs1:19888</value>-->
                <value>172.21.0.23:19888</value>
        </property>
</configuration>
```
修改yarn-site.xml
```xml
<configuration>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>172.21.0.23</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.auxservices.mapreduce.shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
    </property>
    <property>
        <name>yarn.resourcemanager.address</name>
        <value>172.21.0.23:8032</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address</name>
        <value>172.21.0.23:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address</name>
        <value>172.21.0.23:8031</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address</name>
        <value>172.21.0.23:8033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address</name>
        <value>172.21.0.23:8088</value>
    </property>
    <property>
        <name>yarn.nodemanager.resource.memory-mb</name>
        <value>1024</value>
    </property>
</configuration>
```
修改slaves文件,内容改成datanode的ip地址
```bash
lvzhourd-hdfs3
lvzhourd-hdfs4
```
修改pid路径
vim /usr/local/hadoop/etc/hadoop/hadoop-env.sh
注释export HADOOP_PID_DIR=${HADOOP_PID_DIR}，并添该行加如下内容
```bash
export HADOOP_PID_DIR=/home/usr/local/hadoop
```
修改yarn的pid目录
vim sbin/yarn-daemon.sh
在33行，添加如下内容
```bash
YARN_PID_DIR=/home/usr/local/hadoop
```
下述操作在23节点（namenode1）执行执行journalnoded的启动命令
```bash
hadoop.deamon.sh start journalnode
```
初始化jounalnode
```bash
hdfs namenode -initializeSharedEdits
```
初始化namenode
```bash
hdfs namenode -format
```
初始化zkfc
```bash
hdfs zkfc -formatZK
```
### 问题记录
1. 创建HDFS的hadoop用户使用的目录
./bin/hdfs dfs -mkdir -p /user/hadoop
2. 非主节点格式化导致的问题
删掉 /usr/local/hadoop/tmp/
3. 域名问题
How-to: Resolve "Datanode denied communication with namenode because hostname cannot be resolved ip
不解析域名，直接使用IP地址
Resolution:
Add datanode ip/hostname into /etc/hosts. Or enable reverse DNS(Make sure you could get hostname via command: host ip). Or add following property in hdfs-site.xml:
```xml
        <property>
                  <name>dfs.namenode.datanode.registration.ip-hostname-check</name>                   
                  <value>false</value>
        </property>
```
4. hdfs重启
因为hdfs使用的是hadoop用户启动的，重启hadoop时候应该切换到hadoop用户
su hadoop(密码是H3c#lvzhou)
stop-dfs.sh
start-dfs.sh
因为现在有service服务了
5. 离开安全模式
hadoop dfsadmin -safemode leave
6. hadoop查看状态
hdfs dfsadmin -report
查看是否处于safemode状态
hdfs dfsadmin -safemode get
7. node manager配置内存过小，无法启动nodemanager
NodeManager的可用物理内存不允许配置为1024M以下，否则会出现如下错误：
 NodeManager from  data01 doesn't satisfy minimum allocations, Sending SHUTDOWN signal to the NodeManager.
 at org.apache.hadoop.yarn.server.nodemanager.NodeStatusUpdaterImpl.serviceStart(NodeStatusUpdaterImpl.java:196)
修改vim yarn-site.xml
```bash
<property>
    <name>yarn.nodemanager.resource.memory-mb</name>
    <value>1024</value>
    <description>可用的物理内存</description>
</property>
<property>
    <name>yarn.nodemanager.vmem-pmem-ratio</name>
    <value>2</value>
    <description>针对物理内存的虚拟化比例.比如1024M的物理内存,
        此参数配置2,则整体可提供2G内存
    </description>
</property>
```
8. ZKFailoverController: Unable to start failover controller. Parent znode does not exist
修改etc/hadoop/core-site.xml,value值为servicename的值
``` 
<property>
    <name>ha.zookeeper.parent-znode</name>
    <value>/hadoop-ha</value>
</property>
```
9. 修改yarn的pid目录
vim sbin/yarn-daemon.sh
在33行，添加如下内容YARN_PID_DIR=/home/usr/local/hadoop
### 安装参考文档
```bash
http://www.powerxing.com/install-hadoop-cluster/
http://www.powerxing.com/install-hadoop/
```