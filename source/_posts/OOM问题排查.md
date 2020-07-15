---
title: 常见OOM及原因分析
date: 2020-07-06 10:40:30
toc: true
tags: jvm
categories: 
- 技术
---

# 常见OOM类型

## 堆溢出

错误信息：java.lang.OutOfMemoryError: Java heap space 

Java堆用于存储对象实例，只要不断地创建对象，并且保证GC Roots到对象之间有可达路径来避免垃圾回收机制清除这些对象，那么在对象数量到达最大堆的容量限制后就会产生内存溢出异常。 

<!--more-->

### Java 堆溢出排查解决思路

1. 查找关键报错信息，如

```java
java.lang.OutOfMemoryError: Java heap space
```

1. 使用内存映像分析工具（如Eclipsc Memory Analyzer或者Jprofiler）对Dump出来的堆储存快照进行分析，分析清楚是内存泄漏还是内存溢出。
2. 如果是内存泄漏，可进一步通过工具查看泄漏对象到GC	Roots的引用链，修复应用程序中的内存泄漏。
3. 如果不存在泄漏，先检查代码是否有死循环，递归等，再考虑用 -Xmx 增加堆大小。

## 栈溢出

```java
java.lang.OutOfMemoryError: unable to create new native thread
```



关于虚拟机栈和本地方法栈，在Java虚拟机规范中描述了两种异常：

- 如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError 异常；
- 如果虚拟机栈可以动态扩展，当扩展时无法申请到足够的内存时会抛出 OutOfMemoryError 异常。

### 栈溢出原因

- 在单个线程下，栈帧太大，或者虚拟机栈容量太小，当内存无法分配的时候，虚拟机抛出StackOverflowError 异常。
- 不断地建立线程的方式会导致内存溢出

## 方法区溢出

``` JAVA
 java.lang.OutOfMemoryError: Metaspace 
```

 方法区，（又叫永久代，JDK8后，元空间替换了永久代），用于存放Class的相关信息，如类名、访问修饰符、常量池、字段描述、方法描述等。运行时产生大量的类，会填满方法区，造成溢出。 

### 方法区溢出原因

- 使用CGLib生成了大量的代理类，导致方法区被撑爆
- 在Java7之前，频繁的错误使用String.intern方法
- 大量jsp和动态产生jsp
- 应用长时间运行，没有重启

### 方法区溢出排查解决思路

- 检查是否永久代空间设置得过小
- 检查代码是否频繁错误得使用String.intern方法
- 检查是否跟jsp有关。
- 检查是否使用CGLib生成了大量的代理类
- 重启大法，重启JVM

## 本机直接内存溢出

```java
 java.lang.OutOfMemoryError: Direct buffer memory
```

直接内存并不是虚拟机运行时数据区的一部分，也不是Java 虚拟机规范中定义的内存区域。但是，这部分内存也被频繁地使用，而且也可能导致OOM。

在JDK1.4 中新加入了NIO(New Input/Output)类，它可以使用 native 函数库直接分配堆外内存，然后通过一个存储在Java堆中的 DirectByteBuffer 对象作为这块内存的引用进行操作。这样能在一些场景中显著提高性能，因为避免了在 Java 堆和 Native 堆中来回复制数据。

### 直接内存溢出原因

- 本机直接内存的分配虽然不会受到Java 堆大小的限制，但是受到本机总内存大小限制。
- 直接内存由 -XX:MaxDirectMemorySize 指定，如果不指定，则默认与Java堆最大值（-Xmx指定）一样。
- NIO程序中，使用ByteBuffer.allocteDirect(capability)分配的是直接内存，可能导致直接内存溢出。

### 直接内存溢出

- 检查代码是否恰当
- 检查JVM参数-Xmx，-XX:MaxDirectMemorySize 是否合理

## GC overhead limit exceeded

```JAVA
 java.lang.OutOfMemoryError: GC overhead limit exceeded 
```

- 这个是JDK6新加的错误类型，一般都是堆太小导致的。
- Sun 官方对此的定义：超过98%的时间用来做GC并且回收了不到2%的堆内存时会抛出此异常。

### 解决方案

- 检查项目中是否有大量的死循环或有使用大内存的代码，优化代码。
- 检查JVM参数-Xmx -Xms是否合理
- dump内存，检查是否存在内存泄露，如果没有，加大内存。

# OOM常见原因分析

Java服务出现OOM，最常见的原因是：

1. 内存确实分配过小，内存确实不够用；
2. 某一个对象被频繁申请，却没有释放，内存不断泄漏，导致内存耗尽；
3. 某一个资源被频繁申请，系统资源耗尽，例如：不断创建线程，不断发起网络连接；更具体的，可以按照以下步骤，使用以下工具排查。

##  **确认是不是内存本身就分配过小** 

 jmap -heap 10765 

 ![image](/images/oom.png) 

 如上图，可以查看新生代，老生代堆内存的分配大小以及使用情况，看是否本身分配过小。 

## 找到最耗内存的对象

 jmap -histo:live 10765 | more 

 ![image](/images/OOM2.png) 

 需要说明的是，jmap -histo:live 会执行一次FGC，如果仍无法定位，可dump内存，通过Java内存分析工具MAT**（Memory Analyzer Tool**）线下进行分析。 

 上图中占内存最多的对象是RingBufferLogEvent，共占用内存18M，属于正常使用范围。如果发现某类对象占用内存很大（例如几个G），很可能是类对象创建太多，且一直未释放 

例如：
1.申请完资源后，未调用close()或dispose()释放资源；
2.消费者消费速度慢（或停止消费了），而生产者不断往队列中投递任务，导致队列中任务累积过多； 

##  **确认是否是资源耗尽**工具 

1. pstree 
2. netstat 

查看进程创建的线程数，以及网络连接数，如果资源耗尽，也可能出现OOM。这里介绍另一种方法，通过/proc/${PID}/fd 和 /proc/${PID}/task 

 ![image](/images/oom3.png) 

如上图，sshd共占用了四个句柄
（1）0 -> 标准输入；
（2）1 -> 标准输出；
（3）2 -> 标准错误输出；
（4）3 -> socket（容易想到是监听端口）；

sshd只有一个主线程PID为9339，并没有多线程。所以，只要

ll /proc/${PID}/fd | wc -l
ll /proc/${PID}/task | wc -l （效果等同pstree -p | wc -l）
就能知道进程打开的句柄数和线程数。