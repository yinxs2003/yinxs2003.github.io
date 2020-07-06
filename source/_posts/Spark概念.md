---
title: Spark专项训练
date: 2020-07-06 10:40:30
tags: 大数据 
---

# Spark部署模式

1.  standalone模式，开启7077端口提供服务
2.  spark on yarn模式 ：
   1.  client 模式， driver运行在客户端，调试用 
   2. cluster模式， 分布式运行，driver运行在集群子节点 

# RDD 

## 什么是RDD

弹性分布式数据集（RDD），Spark中的基本抽象。 

代表着一种不可变的，可以被并行操作的集合， 这个类包含RDD所有的基本操作，例如map,filter,perssist

## RDD有什么属性

 一组分片

 一个计算每个分区的函数 

 RDD之间的依赖关系 

 一个Partitioner，即RDD的分片函数。 

 一个列表，存储存取每个Partition的优先位置（preferred location） 

## RDD弹性表现在那哪些方面

 自动进行磁盘和内存存储的切换 

 基于lineage的高效容错 

 task执行失败会进行重试 

 stage执行失败会进行重试，并且只重试失败的分片 

 checkpoint和persist数据的持久化缓存 

## RDD的宽依赖窄依赖，stage划分 

窄依赖： 窄依赖就是指父RDD的每个分区只被一个子RDD分区使用 

宽依赖： 宽依赖就是指父RDD的每个分区都有可能被多个子RDD分区使用， 宽依赖（shuffle）由于依赖的上游RDD不止一个，所以往往需要跨节点传输数据。 

stage： 窄依赖会被划分到同一个Stage中，这样它们就能以管道的方式迭代执行 ， 宽依赖往往对应着shuffle操作，当执行算子有shuffle操作的时候，就划分一个Stage，宽依赖是划分stage的依据 

stage容灾： 窄依赖只需要重新执行父RDD的丢失分区的计算即可恢复。 宽依赖则需要考虑恢复所有父RDD的丢失分区，并且同一RDD下的其他分区数据也重新计算了一次。  ，

## RDD持久化 

### Cache

cache是persist(STORAGE_LEVEL=MEMORY_ONLY) 

### Persist持久化策略 

MEMORY_ONLY ： 使用未序列化的Java对象格式，将数据保存在内存中。如果内存不够存放所有的数据，则数据可能就不会进行持久化。那么下次对这个RDD执行算子操作时，那些没有被持久化的数据，需要从源头处重新计算一遍。这是默认的持久化策略，使用cache()方法时，实际就是使用的这种持久化策略。 

MEMORY_ONLY_SER： 基本含义同MEMORY_ONLY。唯一的区别是，会将RDD中的数据进行序列化，RDD的每个partition会被序列化成一个字节数组。这种方式更加节省内存，从而可以避免持久化的数据占用过多内存导致频繁GC。 

MEMORY_AND_DISK ： 使用未序列化的Java对象格式，优先尝试将数据保存在内存中。如果内存不够存放所有的数据，会将数据写入磁盘文件中，下次对这个RDD执行算子时，持久化在磁盘文件中的数据会被读取出来使用。 

MEMORY_AND_DISK_SER ： 基本含义同MEMORY_AND_DISK。唯一的区别是，会将RDD中的数据进行序列化，RDD的每个partition会被序列化成一个字节数组。这种方式更加节省内存，从而可以避免持久化的数据占用过多内存导致频繁GC。 

DISK_ONLY： 使用未序列化的Java对象格式，将数据全部写入磁盘文件中。 

MEMORY_ONLY_2,MEMORY_AND_DISK_2 ： 对于上述任意一种持久化策略，如果加上后缀_2，代表的是将每个持久化的数据，都复制一份副本，并将副本保存到其他节点上。这种基于副本的持久化机制主要用于进行容错。假如某个节点挂掉，节点的内存或磁盘中的持久化数据丢失了，那么后续对RDD计算时还可以使用该数据在其他节点上的副本。如果没有副本的话，就只能将这些数据从源头处重新计算一遍了。 

### Checkpoint

#### 哪些 RDD 需要 checkpoint？ 

 运算时间很长或运算量太大才能得到的 RDD 或是计算链过长或依赖其他 RDD 很多的 RDD 

#### 什么时候进行checkpoint 

cache 机制是每计算出一个要 cache 的 partition 就直接将其 cache 到内存了。但 checkpoint 没有类似的方法，而是等到 job 结束后另外启动专门的 job 去完成 checkpoint 。  

因此 checkpoint 的 RDD 会被计算两次。因此，在使用 rdd.checkpoint() 的时候，建议在该语句前面加上 rdd.cache()，这样第二次运行的 job 就不用再去计算该 rdd 了，直接读取 cache 写磁盘。 

#### Checkpoint和Cache，Persist区别 

##### Checkpoint与Cache的区别

cache把 RDD 计算出来然后放在内存中， 但是RDD 的依赖链也不能丢掉， 当某个点某个 executor 宕了， 上面cache 的RDD就会丢掉， 需要通过依赖链重新计算出来；

checkpoint 是把 RDD 保存在 HDFS中， 是多副本可靠存储，所以依赖链就可以丢掉了，就斩断了依赖链，因为checkpoint是需要把 job 重新从头算一遍， 最好先cache一下， checkpoint就可以直接保存缓存中的 RDD 了， 就不需要重头计算一遍了， 对性能有极大的提升。 

##### Checkpoint与Persist区别

rdd.persist(StorageLevel.DISK_ONLY) 与 checkpoint 区别的是：前者虽然可以将 RDD 的 partition 持久化到磁盘，但该 partition 由 blockManager 管理。一旦 driver program 执行结束，也就是 executor 所在进程 CoarseGrainedExecutorBackend stop，blockManager 也会 stop，被 cache 到磁盘上的 RDD 也会被清空（整个 blockManager 使用的 local 文件夹被删除）。而 checkpoint 将 RDD 持久化到 HDFS 或本地文件夹，如果不被手动 remove 掉（ 话说怎么 remove checkpoint 过的 RDD？ ），是一直存在的，也就是说可以被下一个 driver program 使用，而 cached RDD 不能被其他 dirver program 使用。 

# Spark工作流 

## 提交任务之后发生了什么 

1. 构建Spark Application的运行环境（启动SparkContext） 

2. SparkContext向资源管理器（可以是Standalone、Mesos或YARN）注册并申请运行Executor资源； 
3. 资源管理器分配Executor资源，Executor运行情况将随着心跳发送到资源管理器上；（yarn会分配worker上的资源，worker将运行情况随心跳发送给executor） 
4. SparkContext构建成DAG图，将DAG图分解成Stage，并把Taskset发送给Task Scheduler 
5. Executor向SparkContext申请Task，Task Scheduler将Task发放给Executor运行，SparkContext将应用程序代码发放给Executor。 
6. Task在Executor上运行，运行完毕释放所有资源。 

## Spark组件作用 

master : 管理节点不参与运算 

worker : 分配任务给executor，向master汇报资源使用情况 

driver : 一个Spark作业运行时包括一个Driver进程，也是作业的主进程，具有main函数，并且有SparkContext的实例，是程序的人口点 , 作用：向集群申请资源，向master注册信息，负责作业调度（生成stage层，并将task任务分配到executor上） 

sparkContext : 向yarn申请资源 

client : 提交程序的入口 

##  对于 Spark 中的数据倾斜问题你有什么好的方案？ 

### 什么是数据倾斜

对 Spark/Hadoop 这样的大数据系统来讲，数据量大并不可怕，可怕的是数据倾斜。数据倾斜指的是，并行处理的数据集中，某一部分（如 Spark 或 Kafka 的一个 Partition）的数据显著多于其它部分，从而使得该部分的处理速度成为整个数据集处理的瓶颈（木桶效应）。 

### 数据倾斜是如何造成的 

某个stage中，包含N个task，前N-1个任务执行耗时很短，第N个执行耗时很长，这样导致无法很好利用并行，造成所有任务都在等第N个任务执行完成 

### 解决方案

#### 调整并行度，发生数据倾斜的任务分成多个任务并行执行 

Spark 在做 Shuffle 时，默认使用 HashPartitioner对数据进行分区。如果并行度设置的不合适，可能造成大量不相同的 Key 对应的数据被分配到了同一个 Task 上，造成该 Task 所处理的数据远大于其它 Task，从而造成数据倾斜。

#### 自定义Partitioner

使用自定义的 Partitioner（默认为 HashPartitioner），将原本被分配到同一个 Task 的不同 Key 分配到不同 Task 

#### 将 Reduce side（侧） Join 转变为 Map side（侧） Join 

通过 Spark 的 Broadcast 机制，将 Reduce 侧 Join 转化为 Map 侧 Join，避免 Shuffle 从而完全消除 Shuffle 带来的数据倾斜。 

#### 为数据量特别大的 Key 增加随机前/后缀

为数据量特别大的 Key 增加随机前/后缀，使得原来 Key 相同的数据变为 Key 不相同的数据，从而使倾斜的数据集分散到不同的 Task 中，彻底解决数据倾斜问题。Join 另一则的数据中，与倾斜 Key 对应的部分数据，与随机前缀集作笛卡尔乘积，从而保证无论数据倾斜侧倾斜 Key 如何加前缀，都能与之正常 Join。 

# Shuffle

## 什么是Shuffle

某种具有共同特征的数据汇聚到一个计算节点上进行计算

另一种说法： 将相同的 Key 分发至同一个 Reducer上进行处理 

## 如何避免shuffle

能避免则尽可能避免使用 reduceByKey、join、distinct、repartition 等会进行 shuffle 的算子, 尽量使用 map 类的非 shuffle 算子 