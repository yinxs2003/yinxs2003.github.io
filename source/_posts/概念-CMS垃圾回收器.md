---
title: 概念-CMS垃圾回收器
date: 2021-03-05
toc: true
tags: 概念
categories: 
- 技术
---

### CMS垃圾回收

CMS垃圾回收收集所有代。它会使用最小的资源来进行大多数垃圾回收工作，通常低停顿并发收集器不会复制或者压缩活动的对象。在不移动活动对象的情况下完成垃圾回收。如果内存碎片导致无法正常分配内存，请分配更大的堆内存。

CMS在年老带执行垃圾收集会氛围以下几个阶段

| Phase                                     | Description                                                  |
| :---------------------------------------- | ------------------------------------------------------------ |
| (1) Initial Mark *(Stop the World Event)* | Objects in old generation are “marked” as reachable including those objects which may be reachable from young generation. Pause times are typically short in duration relative to minor collection pause times. |
| (2) Concurrent Marking                    | Traverse the tenured generation object graph for reachable objects concurrently while Java application threads are executing. Starts scanning from marked objects and transitively marks all objects reachable from the roots. The mutators are executing during the concurrent phases 2, 3, and 5 and any objects allocated in the CMS generation during these phases (including promoted objects) are immediately marked as live. |
| (3) Remark *(Stop the World Event)*       | Finds objects that were missed by the concurrent mark phase due to updates by Java application threads to objects after the concurrent collector had finished tracing that object. |
| (4) Concurrent Sweep                      | Collects the objects identified as unreachable during marking phases. The collection of a dead object adds the space for the object to a free list for later allocation. Coalescing of dead objects may occur at this point. Note that live objects are not moved. |
| (5) Resetting                             | Prepare for next concurrent collection by clearing data structures. |

### 查看垃圾收集步骤

我们来一步一步查看CMS垃圾收集器的动作：

1. CMS的堆结构

   堆被分成如下三个部分

   ![img](/images/jvm-Slide1.png)

   年轻代被划分成多个区域，分别是一个Eden区和两个survivor区。

   年老代被划分成一整个连续的区域，年老代垃圾回收通常在原位置进行回收，除非有full GC，否则年老代不进行压缩。

2. CMS在年轻代GC如何工作

   ![img](/images/jvm-Slide2.png)

   在下图，年轻代是绿色表示，年老代是蓝色表示。如果你的程序已经运行了一段时间，CMS有可能是这样，对象在年老代是散落分布的。

   对于CMS的年老代的内存释放，如果不是FullGC，不会进行内存空间压缩。

3. 收集年轻代

   活着的对象从Eden区和survivior区拷贝到另一个survivor区。任何旧对象达到年龄阈值都会被允许进入老年代

   ![jvm-Slide3.png](/images/jvm-Slide3.png)

4. 年轻代收集之后

   年轻代收集之后，Eden区被清理，且两个survivor中的一个被清空

   ![jvm-Slide4.png](/images/jvm-Slide4.png)

   新提升对象在图中以深蓝色表示。绿色代表是年轻代存活且没有被允许进入老年代的对象

5. CMS收集器的年老代

   两次Stop the world发生在：初始化标记和重新标记。当年老代达到一定占用率，CMS开始收集垃圾。

   ![jvm-Slide5.png](/images/jvm-Slide5.png)

   （1）可达的对象被标记时候，初始化标记会发生短暂stop-the-world现象、

   （2）并发标记寻找存活的对象并继续执行寻找

   （3）在重新标记阶段，会重新标记在并发标记阶段中没有标记且满足被标记条件的对象

6. 年老代收集 - 并发清除

   对象没有再上一个阶段被标记将会被释放，且没有内存区域压缩

   ![jvm-Slide6.png](/images/jvm-Slide6.png)

   注意：Unmarked objects == Dead Objects

7. 年老代收集 - 清除之后

   在（4）清除阶段，你可以看到大量的内存被释放，并且可以看出没有执行压缩内存操作

   ![jvm-Slide7.png](/images/jvm-Slide7.png)

​        最后CMS收集器将进入（5）重置阶段，并且等待达到下次GC阈值

### 参考

[G1垃圾回收器]()