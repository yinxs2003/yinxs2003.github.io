---
title: Java-自定义注解（二）
date: 2020-11-05
toc: true
tags: 技术Java
categories: 
- 技术
---

再读完Java自定义注解（一）我们可以通过代码来实现我们自己的自定义注解

#### 如何自定义注解

EventBean类

```JAVA
package com.code.note.annotation;

import lombok.Data;

@Data
public class EventBean {

    @EventName("coding now...")
    private String name;

    @EventType(eventType = EventType.Type.MEETING)
    private String type;

    @User(id = 1, name = "testName", email = "15090552277@163.com")
    private String user;
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

