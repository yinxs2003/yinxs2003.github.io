---
title: Flink窗口
date: 2020-07-03 12:50:30
tags: 大数据
---

# 什么是窗口

Windows是处理流试计算的核心。 Windows将流分成有限个大小的“存储桶”，我们可以在“存储桶”上应用计算。

# 窗口类型

## Tumbling Window

翻滚窗口，无数据重叠

滚动窗口分配器将每个元素分配给指定窗口大小的窗口。 滚动窗口具有固定的大小，并且不重叠。 例如，如果您指定大小为5分钟的翻滚窗口，则将评估当前窗口，并且每五分钟将启动一个新窗口，如下图所示。

<!--more-->

![tumbling-windows](/images/tumbling-windows.svg)

## Sliding Window

滑动窗口，可数据重叠

滑动窗口分配器将元素分配给固定长度的窗口。和滚动窗口相比，窗口大小可以被windows param参数控制，一个额外的滑动参数控制滑动窗口的滑动频率。因此滑动窗口是可以重复的当滑动大小小于窗口大小。在这种情况下，元素可以被分配给多个窗口（可以重复）

例如：你可以存在一组长度为10分钟的窗口集合，这样你可以每隔5分钟就得到下一个窗口，且包含最近10分钟的所有事件，示意图如下所示

![sliding-windows](/images/sliding-windows.svg)

## Session Window

Session Window，活动时间间隙相等的窗口

会话窗口分配器按活动会话对元素进行分组。与滚动窗口和滑动窗口相比，会话窗口不重叠且没有固定的开始和结束时间。 相反，当会话窗口在一定时间段内未接收到元素时（即，发生不活动间隙时），它将关闭。 会话窗口分配器可以配置有静态会话间隔，也可以配置有会话间隔提取器功能，该功能定义不活动的时间长度。 当该时间段到期时，当前会话将关闭，随后的元素将分配给新的会话窗口。

![sliding-windows](/images/session-windows.svg)

## Global Windows

Global Windows 全局窗口

全局窗口分配器将具有相同键的所有元素分配给同一单个全局窗口。 仅当您还指定自定义触发器时，此窗口方案才有用。 否则，将不会执行任何计算，因为全局窗口没有可以处理聚合元素的自然终点。

# 窗口函数

在定义窗口分配器后，我们需要指定每个窗口上执行的计算。一旦系统处于就绪状态，就可以处理每个（可能是键控）窗口中的元素，这是窗口函数的职能所在（请参阅Flink如何确定窗口何时准备就绪的触发器）

窗口函数可以是ReduceFunction, AggregateFunction, FoldFunction or ProcessWindowFunction中的一种，前两个性能更高，因为在每个窗口中元素到达的时候flink是增量聚合的。一个ProcessWindowFunction函数可以为窗口中的元素获取一个迭代器，该迭代器包含一个窗口且包含窗口中元素的额外信息

使用ProcessWindowFunction进行窗口转换不能像其他情况一样有效地执行，因为Flink必须在调用函数之前在内部缓冲窗口的所有元素。可以通过将ProcessWindowFunction与ReduceFunction，AggregateFunction或FoldFunction组合使用来获得窗口元素的增量聚合以及ProcessWindowFunction接收的其他窗口元数据，从而缓解这种情况。代码示例如下。

## ReduceFunction

一个ReduceFunction函数指定了如何将输入中的两个元素组合，且输出一个相同类型的结果。Flink使用ReduceFunction来增量聚合窗口中的元素。

一个ReduceFunction函数可以这样来使用

```java
DataStream<Tuple2<String, Long>> input = ...;

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .reduce(new ReduceFunction<Tuple2<String, Long>> {
      public Tuple2<String, Long> reduce(Tuple2<String, Long> v1, Tuple2<String, Long> v2) {
        return new Tuple2<>(v1.f0, v1.f1 + v2.f1);
      }
    });
```

上面的例子操作是将Tuple第二个位置上的元素相加

## AggregateFunction

AggregateFunction是ReduceFunction的通用版本，具有三种类型：输入类型（IN），累加器类型（ACC）和输出类型（OUT）。输入类型是输入流中元素的类型，AggregateFunction具有一种将一个输入元素添加到累加器的方法。 该接口还具有创建初始累加器，将两个累加器合并为一个累加器以及从累加器提取输出（OUT类型）的方法。 我们将在下面的示例中看到它的工作原理。

与ReduceFunction相同，Flink将在窗口的输入元素到达时对其进行增量聚合。

```java
/**
 * 计算窗口中元素的第二个字段的平均值。
 */
private static class AverageAggregate
         implements AggregateFunction<Tuple2<String, Long>, Tuple2<Long, Long>, Double> {
  
  @Override
  public Tuple2<Long, Long> createAccumulator() {
    return new Tuple2<>(0L, 0L);
  }

  @Override
  public Tuple2<Long, Long> add(Tuple2<String, Long> value, Tuple2<Long, Long> accumulator){
    return new Tuple2<>(accumulator.f0 + value.f1, accumulator.f1 + 1L);
  }

  @Override
  public Double getResult(Tuple2<Long, Long> accumulator) {
    return ((double) accumulator.f0) / accumulator.f1;
  }

  @Override
  public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
    return new Tuple2<>(a.f0 + b.f0, a.f1 + b.f1);
  }
}

DataStream<Tuple2<String, Long>> input = ...;

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .aggregate(new AverageAggregate());
```

## FoldFunction

## ProcessWindowFunction

ProcessWindowFunction获取一个Iterable，该Iterable包含窗口的所有元素，以及一个Context对象，该对象可以访问时间和状态信息，从而使其比其他窗口函数更具灵活性。 这是以性能和资源消耗为代价的，因为无法增量聚合元素，而是需要在内部对其进行缓冲，直到将窗口视为已准备好进行处理为止。



```java
DataStream<Tuple2<String, Long>> input = ...;

input
  .keyBy(t -> t.f0)
  .timeWindow(Time.minutes(5))
  .process(new MyProcessWindowFunction());

/* ... */

public class MyProcessWindowFunction 
    extends ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow> {

  @Override
  public void process(String key, Context context, Iterable<Tuple2<String, Long>> input, Collector<String> out) {
    long count = 0;
    for (Tuple2<String, Long> in: input) {
      count++;
    }
    out.collect("Window: " + context.window() + "count: " + count);
  }
}
```

该示例显示了一个ProcessWindowFunction，它对窗口中的元素进行计数。 另外，窗口功能将有关窗口的信息添加到输出中。

注意请注意，将ProcessWindowFunction用于简单的聚合（如count）效率很低。 下一节说明如何将ReduceFunction或AggregateFunction与ProcessWindowFunction结合使用，以获取增量聚合和ProcessWindowFunction的附加信息。

## ProcessWindowFunction with Incremental Aggregation

可以将ProcessWindowFunction与ReduceFunction，AggregateFunction或FoldFunction组合以在元素到达窗口时对其进行增量聚合。 当窗口关闭时，将向ProcessWindowFunction提供聚合结果。 这使得它可以递增地计算窗口，同时可以访问ProcessWindowFunction的其他窗口元信息。

#### 使用ReduceFunction的增量窗口聚合

以下示例显示了如何将增量ReduceFunction与ProcessWindowFunction结合使用以返回窗口中的最小事件以及该窗口的开始时间。

```java
DataStream<SensorReading> input = ...;

input
  .keyBy(<key selector>)
  .timeWindow(<duration>)
  .reduce(new MyReduceFunction(), new MyProcessWindowFunction());

// Function definitions

private static class MyReduceFunction implements ReduceFunction<SensorReading> {

  public SensorReading reduce(SensorReading r1, SensorReading r2) {
      return r1.value() > r2.value() ? r2 : r1;
  }
}

private static class MyProcessWindowFunction
    extends ProcessWindowFunction<SensorReading, Tuple2<Long, SensorReading>, String, TimeWindow> {

  public void process(String key,
                    Context context,
                    Iterable<SensorReading> minReadings,
                    Collector<Tuple2<Long, SensorReading>> out) {
      SensorReading min = minReadings.iterator().next();
      out.collect(new Tuple2<Long, SensorReading>(context.window().getStart(), min));
  }
}
```

## Using per-window state in ProcessWindowFunction

# Triggers

# Evictors

# Allowed Lateness