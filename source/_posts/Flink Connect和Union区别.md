---
title: Flink Connect和Union区别
date: 2020-11-03 10:40:30
toc: true
tags: Java 
categories: 
- 技术
---

`Flink DataStream`中`union`和`connect`都有一个共同的作用，就是将2个流或多个流合成一个流。但是两者的区别是：`union`连接的2个流的类型必须一致，`connect`连接的流可以不一致，但是可以统一处理。

具体看下面示例：

```java
public class ConnectOperator {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        sEnv.setParallelism(1);

        Properties p = new Properties();
        p.setProperty("bootstrap.servers", "localhost:9092");

        SingleOutputStreamOperator<Student> student = sEnv
                .addSource(new FlinkKafkaConsumer010<String>("student", new SimpleStringSchema(), p))
                .map(new MapFunction<String, Student>() {
                    @Override
                    public Student map(String value) throws Exception {
                        return new Gson().fromJson(value, Student.class);
                    }
                });

        student.print();

        SingleOutputStreamOperator<Teacher> teacher = sEnv
                .addSource(new FlinkKafkaConsumer010<String>("teacher", new SimpleStringSchema(), p))
                .map(new MapFunction<String, Teacher>() {
                    @Override
                    public Teacher map(String value) throws Exception {
                        return new Gson().fromJson(value, Teacher.class);
                    }
                });

        teacher.print();

        ConnectedStreams<Student, Teacher> connect = student.connect(teacher);

        connect.process(new CoProcessFunction<Student, Teacher, Tuple5<String, Integer, String, String, Long>>() {
            @Override
            public void processElement1(Student value, Context ctx, Collector<Tuple5<String, Integer, String, String, Long>> out) throws Exception {
                out.collect(new Tuple5<>(value.name, value.age, value.sex, value.classId, value.timestamp));
            }

            @Override
            public void processElement2(Teacher value, Context ctx, Collector<Tuple5<String, Integer, String, String, Long>> out) throws Exception {
                out.collect(new Tuple5<>(value.name, value.age, value.sex, value.classId, value.timestamp));
            }
        }).print("process");

        // connect
        connect.map(new CoMapFunction<Student, Teacher, Tuple5<String, Integer, String, String, Long>>() {
            @Override
            public Tuple5<String, Integer, String, String, Long> map1(Student value) throws Exception {
                return new Tuple5<>(value.name, value.age, value.sex, value.classId, value.timestamp);
            }

            @Override
            public Tuple5<String, Integer, String, String, Long> map2(Teacher value) throws Exception {
                return new Tuple5<>(value.name, value.age, value.sex, value.classId, value.timestamp);
            }
        }).print("map");


        // union
        student.map(new MapFunction<Student, Tuple5<String, Integer, String, String, Long>>() {
            @Override
            public Tuple5<String, Integer, String, String, Long> map(Student value) throws Exception {
                return new Tuple5<>(value.name, value.age, value.sex, value.classId, value.timestamp);
            }
        }).union(teacher.map(new MapFunction<Teacher, Tuple5<String, Integer, String, String, Long>>() {
            @Override
            public Tuple5<String, Integer, String, String, Long> map(Teacher value) throws Exception {
                return new Tuple5<>(value.name, value.age, value.sex, value.classId, value.timestamp);
            }
        })).print("union");


        sEnv.execute("ConnectOperator");
    }
}
```

connect可以将2个不同类型的流同时用不同的逻辑处理好，形成一个流。

union是将2个同类型的流，合成一个，进行处理。

