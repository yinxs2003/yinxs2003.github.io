---
title: Java-自定义注解
date: 2020-11-05
toc: true
tags: 算法
categories: 
- 技术
---

#### 元注解@Target

最常用的元注解是`@Target`。使用`@Target`可以定义`Annotation`能够被应用于源码的哪些位置：

- 类或接口：`ElementType.TYPE`；
- 字段：`ElementType.FIELD`；
- 方法：`ElementType.METHOD`；
- 构造方法：`ElementType.CONSTRUCTOR`；
- 方法参数：`ElementType.PARAMETER`。

例如，定义注解`@Report`可用在方法上，我们必须添加一个`@Target(ElementType.METHOD)`：

#### 如何自定义注解

第一步，用`@interface`定义注解：

```JAVA
public @interface Report {
}

```

<!--more-->

第二步，添加参数、默认值：

```java
public @interface Report {
    int type() default 0;
    String level() default "info";
    String value() default "";
}
```

第三步，用元注解配置注解：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Report {
    int type() default 0;
    String level() default "info";
    String value() default "";
}
```

其中，必须设置`@Target`和`@Retention`，`@Retention`一般设置为`RUNTIME`，因为我们自定义的注解通常要求在运行期读取。一般情况下，不必写`@Inherited`和`@Repeatable`。

