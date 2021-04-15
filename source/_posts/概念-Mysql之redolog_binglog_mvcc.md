---
title: 概念-Mysql之redolog_binglog_mvcc.md
date: 2021-04-14
toc: true
tags: 概念
categories: 
- 技术
---

## 前置概念

1. 数据库中，数据在内存中叫data buffer，数据在磁盘上叫data file。事务的日志也一样，在内存中叫log  buffer，在磁盘上叫log file。

2. data buffer中的数据会在合适的时间 由存储引擎写入到data file。并不在事务提交时机制中。

3. checkpoint

   checkpoint是为了定期将db buffer的内容刷新到data file。

   当遇到内存不足、db buffer已满等情况时，需要将db buffer中的内容/部分内容（特别是脏数据）转储到data file中。

   在转储时，会记录checkpoint发生的”时刻“。在故障恢复时候，**只需要redo/undo最近的一次checkpoint之后的操作**。

## RedoLog

### 概念

记录事务执行后的状态，用来恢复未写入磁盘的数据（data file）的已成功事务更新的数据。

例如某一事务的事务序号为T1，其对数据X进行修改，设X的原值是5，修改后的值为15，那么Redo日志为15。

RedoLog大多数是物理日志。

### 作用

1. 保证了事务的**持久性**。

2. 数据库崩溃时回复。如：A字段本来=1，设置A字段=5，内存中的数据data buffer已经改了，redo log也刷入磁盘中了，事务也提交了，但是还没来及的把A字段=5刷到磁盘中，数据库就挂了，那再恢复了数据库看到的A字段就是=1，这时就用redo log来**回放**把A变成=5

3. 流程

   以一个事务为例

   ![【八】MySQL事务之MVCC、undo、redo、binlog、二阶段提交1](https://res-static.hc-cdn.cn/fms/img/51b37c39d64d34133b71267db37c9d091603433299270.png)

   重点：

   1. 修改后做redo log记录。

   2. log buffer刷入磁盘后，才提交事务。

   3. 这种先持久化日志的策略叫做Write Ahead Log，即预写日志。

## UndoLog

### UndoLog概念

用于记录事务开始前的状态，用于事务失败时的**回滚**操作。

例如某一事务的事务序号为T1，其对数据X进行修改，设X的原值是5，修改后的值为15，那么Undo日志为5。

UndoLog是**逻辑日志**

**重点：**

1. 修改前做undo log记录

2. undo log刷入磁盘后，才提交事务。

### UndoLog作用

1. 保证了事务的**原子性（回滚）**

2. 实现MVCC

3. 保证普通select快照读

### UndoLog中分为两种类型

1. INSERT_UNDO（INSERT操作），记录插入的唯一键值；

2. UPDATE_UNDO（包含UPDATE及DELETE操作），记录修改的唯一键值以及old column记录。

### 举个UndoLog和RedoLog合在一起的例子

 假设有A、B两个数据，值分别是 1 和 2，在一个事务中先后把A设置为3，B设置为4。

1. 事务开始

2. 记录A=1到undo log buffer

3. 修改A=3

4. 记录A=3到redo log buffer

5. 记录B=2到undo log buffer

6. 修改B=4

7. 记录B=4到redo log buffer

8. 到log buffer全部刷入到磁盘中后才提交数据

这里有个点，log buffer 刷入到磁盘，并**不是最后要提交事物了才来一次性全部刷入到磁盘**。log buffer刷入到log file是在事务进行的时候就逐步在做了。 

## 恢复以及无法恢复情况

事务**无法回滚**，无法回放的情况 ：

1. innodb_flush_log_at_trx_commit=2时，将redo日志写入logfile后，为提升事务执行的性能，存储引擎并没有调用文件系统的sync操作，将日志落盘。如果此时宕机了，那么未落盘redo日志事务的数据是无法保证一致性的。

2. UndoLog同样存在未落盘的情况，可能出现无法回滚的情况。 

   未提交的事务和回滚了的事务也会记录Redo Log，因此在**进行恢复**时,这些事务要进行特殊的的处理。进行恢复的策略如下

有2种不同的**恢复策略**：

1. 进行恢复时，只重做已经提交了的事务。
2. 进行恢复时，重做所有事务包括未提交的事务和回滚了的事务。然后通过Undo Log回滚那些未提交的事务。

## Binlog 

### Binlog作用

1. 用于主从复制，在主从复制中，从库利用主库上的binlog进行重播，实现主从同步。
2. 用于数据库的基于时间点的还原。

### BinLog记录了哪些内容

记录的是执行过的事务中的sql语句和包括了执行的sql语句（增删改）反向的信息，比如：delete对应着delete本身和其反向的insert；update对应着update执行前后的版本的信息；insert对应着delete和insert本身的信息。

### Binlog是如何生成的

事务提交的时候一次性将事务中的sql语句按照一定的格式记录到binlog中（一个事物可能对应多个sql语句）。

Binlog与RedoLog很明显的差异就是RedoLog并不一定是在事务提交的时候刷新到磁盘，RedoLog是在事务开始之后就开始逐步写入磁盘。

开启了Binlog的情况下，对于较大事务的提交，可能会变得比较慢一些。

### Binlog的删除

binlog的默认是保持时间由参数expire_logs_days配置，对于非活动的日志文件，在生成时间超过expire_logs_days配置的天数之后会被自动删除。

### Binlog与RedoLog区别

1. 作用不同

   RedoLog是保证事物持久性的，是事物层面的，RedoLog通常用来恢复事务。

   Binlog作为还原功能是数据库层面的，Binlog通常用来在slave库重放

2. 作用不同

   RedoLog是物理日志

   Binlog是逻辑日志

   恢复时候RedoLog效率高于Binlog

3. 两者日志产生的时间，可以释放的时间，在可释放的情况下清理机制，都是完全不同的

### BinLog保证主从一致性

为了保证主从复制时候的主从一致（当然也包括使用binlog进行基于时间点还原的情况），事务提交时，RedoLog和binlog的写入顺序，是要严格一致的，
MySQL通过两阶段提交过程来完成事务的一致性的，也即RedoLog和binlog的一致性的，理论上是先写RedoLog，再写binlog，两个日志都提交成功（刷入磁盘），事务才算真正的完成。

## 二阶段提交

RedoLog保证了事务的一致性，Binlog保证了主从复制的一致性

先进入commit prepare 阶段：事务中新生成的RedoLog会被刷到磁盘，并将回滚段置为prepared状态。binlog不作任何操作，存储引擎写RedoLog

![image-20210414111242946](/Users/yxs1112003/Library/Application Support/typora-user-images/image-20210414111242946.png)

commit阶段：innodb释放锁，释放回滚段，设置RedoLog提交状态**，**binlog持久化到磁盘，然后存储引擎层提交，Server层写binlog

![img](https://img-blog.csdnimg.cn/20200417153438295.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p5MDIyNjg4Nzk=,size_16,color_FFFFFF,t_70)

## MVCC

### 什么是MVCC

MVCC的全称是“多版本并发控制”。这项技术使得InnoDB的事务隔离级别下执行一致性读操作有了保证。

换言之，就是为了查询一些正在被另一个事务更新的行，并且可以看到它们被更新之前的值。

MVCC由于其实现原理,只支持read committed和repeatable read隔离等级

### MVCC作用

1. 增强并发性。这样的一来查询就不用等待另一个事务释放锁。

2. 在可重复读的隔离级别中，保障了单纯的select(不会加锁)的“可重复读”特性

这项技术在数据库领域并不是普遍使用的，其它的数据库产品，以及mysql其它的存储引擎并不支持它。

### 快照读、当前读、

快照读：读取的是快照版本，也就是历史版本。如：普通的SELECT

当前读：读取的是最新版本，如：UPDATE、DELETE、INSERT、SELECT ...  LOCK IN SHARE MODE、SELECT ... FOR UPDATE是当前读。

### 锁定读

​       在一个事务中，标准的SELECT语句是不会加锁，但是有两种情况例外。

​		SELECT ... LOCK IN SHARE MODE

　　给记录加设共享锁，这样一来的话，其它事务只能读不能修改，直到当前事务提交

　　SELECT ... FOR UPDATE

　　给记录加排它锁，这种情况下跟UPDATE的加锁情况是一样的

### 一致性非锁定读

事务中的单纯、标准的select读是不加锁的。而这个单纯的select不加锁的读就是**一致性非锁定读**，一致性非锁定读就是MVCC来保证的 

一致性读（consistent read），InnoDB用多版本来提供查询数据库在某个时间点的快照

> 如果隔离级别是REPEATABLE READ，那么在同一个事务中的所有一致性读都读的是事务中第一个这样的读读到的快照；
>
> 如果隔离级别是READ COMMITTED，那么一个事务中的每一个一致性读都会读到它自己刷新的快照版本。

Consistent read（一致性读）是READ COMMITTED和REPEATABLE READ隔离级别下普通SELECT语句默认的模式，一致性读不会给它所访问的表加任何形式的锁，因此其它事务可以同时并发的修改它们。 

