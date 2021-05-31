---
title: Java-jstack查看线程状态
date: 2021-05-26
toc: true
tags: 技术Java
categories: 
- 技术
---

有些时候我们需要查看下jvm中的线程执行情况，比如，发现服务器的CPU的负载突然增

高了、出现了死锁、死循环等，我们该如何分析呢？

由于程序是正常运行的，没有任何的输出，从日志方面也看不出什么问题，所以就需要

看下jvm的内部线程的执行情况，然后再进行分析查找出原因。

用法：jstack <pid>

![img](/images/jstack-1.png)

java线程的6种状态

**初始态（NEW）**

创建一个Thread对象，但还未调用start()启动线程时，线程处于初始态。

**运行态（RUNNABLE），在Java中，运行态包括 就绪态 和 运行态。**

就绪态

该状态下的线程已经获得执行所需的所有资源，只要CPU分配执行权就能运行。

所有就绪态的线程存放在就绪队列中。

运行态

获得CPU执行权，正在执行的线程。

由于一个CPU同一时刻只能执行一条线程，因此每个CPU每个时刻只有一条运行态的线程。

**阻塞态（BLOCKED）**

当一条正在执行的线程请求某一资源失败时，就会进入阻塞态。

而在Java中，阻塞态专指请求锁失败时进入的状态。

由一个阻塞队列存放所有阻塞态的线程。

处于阻塞态的线程会不断请求资源，一旦请求成功，就会进入就绪队列，等待执行。

**等待态（WAITING）**

当前线程中调用wait、join、park函数时，当前线程就会进入等待态。

也有一个等待队列存放所有等待态的线程。

线程处于等待态表示它需要等待其他线程的指示才能继续运行。

进入等待态的线程会释放CPU执行权，并释放资源（如：锁）

**超时等待态（TIMED_WAITING）**

当运行中的线程调用sleep(time)、wait、join、parkNanos、parkUntil时，就会进入该状态；它和等待态一样，并不是因为请求不到资源，而是主动进入，并且进入后需要其他线程唤醒；

进入该状态后释放CPU执行权 和 占有的资源。

与等待态的区别：到了超时时间后自动进入阻塞队列，开始竞争锁。

**终止态（TERMINATED）**

线程执行结束后的状态。

**使用jstack查找死锁**

构造死锁

```java
public class TestDeadLock {
    private static Object obj1 = new Object();
    private static Object obj2 = new Object();

    public static void main(String[] args) {
        new Thread(new Thread1()).start();
        new Thread(new Thread2()).start();
    }

    private static class Thread1 implements Runnable {
        @Override
        public void run() {
            synchronized (obj1) {
                System.out.println("Thread1 拿到了 obj1 的锁！");
                try {
                    // 停顿2秒的意义在于，让Thread2线程拿到obj2的锁
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj2) {
                    System.out.println("Thread1 拿到了 obj2 的锁！");
                }
            }
        }
    }

    private static class Thread2 implements Runnable {
        @Override
        public void run() {
            synchronized (obj2) {
                System.out.println("Thread2 拿到了 obj2 的锁！");
                try {
                    // 停顿2秒的意义在于，让Thread1线程拿到obj1的锁
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj1) {
                    System.out.println("Thread2 拿到了 obj1 的锁！");
                }
            }
        }
    }
}
```

jstack 11464

![img](/images/jstack-2.png)

```text
Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x0000017bfeca3e80 (object 0x000000008a0e36e8, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x0000017bfeca5e80 (object 0x000000008a0e36f8, a java.lang.Object),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
        at TestDeadLock$Thread2.run(TestDeadLock.java:40)
        - waiting to lock <0x000000008a0e36e8> (a java.lang.Object)
        - locked <0x000000008a0e36f8> (a java.lang.Object)
        at java.lang.Thread.run(java.base@9.0.4/Thread.java:844)
"Thread-0":
        at TestDeadLock$Thread1.run(TestDeadLock.java:22)
        - waiting to lock <0x000000008a0e36f8> (a java.lang.Object)
        - locked <0x000000008a0e36e8> (a java.lang.Object)
        at java.lang.Thread.run(java.base@9.0.4/Thread.java:844)

Found 1 deadlock.
```