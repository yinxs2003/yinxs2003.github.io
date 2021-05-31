---
title: 概念-在Spring中如何解决循环依赖的问题
date: 2021-05-31
toc: true
tags: 概念
categories: 
- 技术
---

## 什么是循环依赖

一般场景是一个Bean A依赖Bean B,而Bean B也依赖Bean A.
Bean A → Bean B → Bean A

当然我们也可以添加更多的依赖层次，比如：
Bean A → Bean B → Bean C → Bean D → Bean E → Bean A

## Spring发生了什么

当 Spring 上下文加载所有 bean 时，它会尝试按照它们完全工作所需的顺序创建 bean。例如，如果我们没有循环依赖，就像下面的例子：

bean A → bean B → bean C

Spring将创建bean C，然后创建bean B（并将bean C注入其中），然后创建bean A（并将bean B注入其中）。

但是，当有循环依赖时，Spring 无法决定首先创建哪个 bean，因为它们相互依赖。在这些情况下，Spring 将在加载上下文时引发*BeanCurrentlyInCreationException*。

在 Spring 中使用**构造函数注入**时可能会发生这种情况；如果您使用其他类型的注入，您应该不会发现此问题，因为依赖项将在需要时注入，而不是在上下文加载时注入。

## 一个简单的例子

让我们定义两个相互依赖的 bean（通过构造函数注入）：

```java
@Component
public class CircularDependencyA {

    private CircularDependencyB circB;

    @Autowired
    public CircularDependencyA(CircularDependencyB circB) {
        this.circB = circB;
    }
}
@Component
public class CircularDependencyB {

    private CircularDependencyA circA;

    @Autowired
    public CircularDependencyB(CircularDependencyA circA) {
        this.circA = circA;
    }
}
```

现在我们可以为测试编写一个 Configuration 类，我们称之为*TestConfig*，它指定要扫描组件的基本包。假设我们的 bean 定义在包“ *com.baeldung.circulardependency* ”中：

```java
@Configuration
@ComponentScan(basePackages = { "com.baeldung.circulardependency" })
public class TestConfig {
}
```

最后我们可以编写一个 JUnit 测试来检查循环依赖。测试可以为空，因为在上下文加载期间将检测到循环依赖。

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class CircularDependencyTest {

    @Test
    public void givenCircularDependency_whenConstructorInjection_thenItFails() {
        // Empty test; we just want the context to load
    }
}
```

如果您尝试运行此测试，您将收到以下异常：

```java
BeanCurrentlyInCreationException: Error creating bean with name 'circularDependencyA':
Requested bean is currently in creation: Is there an unresolvable circular reference?
```

## 应对办法

### 重新设计

当您具有循环依赖关系时，可能是您遇到了设计问题，并且职责没有很好地分开。您应该尝试正确地重新设计组件，以使它们的层次结构设计得很好，并且不需要循环依赖项。

如果您无法重新设计组件（可能有很多可能的原因：遗留代码、已经过测试且无法修改的代码、没有足够的时间或资源进行完整的重新设计……），可以尝试一些变通方法。

### 使用@Lazy

打破循环的一种简单方法是说 Spring 懒惰地初始化其中一个 bean。也就是说：它不会完全初始化 bean，而是创建一个代理以将其注入另一个 bean。注入的Bean仅在首次需要时才完全创建。

要使用我们的代码尝试此操作，您可以将 CircularDependencyA 更改为以下内容：

```java
@Component
public class CircularDependencyA {

    private CircularDependencyB circB;

    @Autowired
    public CircularDependencyA(@Lazy CircularDependencyB circB) {
        this.circB = circB;
    }
}
```

如果您现在运行测试，您将看到这次错误不会发生。

### 使用 Setter/Field 注入

最流行的解决方法之一，也是[Spring 文档提出的](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html)，是使用 setter 注入。

简单地说，如果您更改 bean 的连接方式以使用 setter 注入（或字段注入）而不是构造函数注入 - 这确实解决了问题。通过这种方式，Spring 创建了 bean，但在需要它们之前不会注入依赖项。

让我们这样做——让我们改变我们的类以使用 setter 注入，并将另一个字段 ( *message* )添加到*CircularDependencyB*以便我们可以进行适当的单元测试：

```java
@Component
public class CircularDependencyA {

    private CircularDependencyB circB;

    @Autowired
    public void setCircB(CircularDependencyB circB) {
        this.circB = circB;
    }

    public CircularDependencyB getCircB() {
        return circB;
    }
}
@Component
public class CircularDependencyB {

    private CircularDependencyA circA;

    private String message = "Hi!";

    @Autowired
    public void setCircA(CircularDependencyA circA) {
        this.circA = circA;
    }

    public String getMessage() {
        return message;
    }
}
```

现在我们必须对单元测试进行一些更改：

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class CircularDependencyTest {

    @Autowired
    ApplicationContext context;

    @Bean
    public CircularDependencyA getCircularDependencyA() {
        return new CircularDependencyA();
    }

    @Bean
    public CircularDependencyB getCircularDependencyB() {
        return new CircularDependencyB();
    }

    @Test
    public void givenCircularDependency_whenSetterInjection_thenItWorks() {
        CircularDependencyA circA = context.getBean(CircularDependencyA.class);

        Assert.assertEquals("Hi!", circA.getCircB().getMessage());
    }
}
```

下面解释上面看到的注释：

*@Bean*：告诉 Spring 框架必须使用这些方法来检索要注入的 bean 的实现。

*@Test*：测试将从上下文中获取 CircularDependencyA bean 并断言其 CircularDependencyB 已正确注入，检查其*message*属性的值。

### 使用@PostConstruct

打破循环的另一种方法是在其中一个 bean 上使用*@Autowired*注入一个依赖项，然后使用一个用*@PostConstruct*注释的方法来设置另一个依赖项。

我们的 bean 可以有以下代码：

```java
@Component
public class CircularDependencyA {

    @Autowired
    private CircularDependencyB circB;

    @PostConstruct
    public void init() {
        circB.setCircA(this);
    }

    public CircularDependencyB getCircB() {
        return circB;
    }
}
@Component
public class CircularDependencyB {

    private CircularDependencyA circA;
	
    private String message = "Hi!";

    public void setCircA(CircularDependencyA circA) {
        this.circA = circA;
    }
	
    public String getMessage() {
        return message;
    }
}
```

我们可以运行与之前相同的测试，因此我们检查循环依赖异常仍然没有被抛出并且依赖被正确注入。

### 实现ApplicationContextAware和InitializingBean

如果其中一个 bean 实现了*ApplicationContextAware*，则该 bean 可以访问 Spring 上下文并可以从那里提取另一个 bean。实现*InitializingBean*我们表明这个 bean 在它的所有属性都被设置后必须做一些动作；在这种情况下，我们想手动设置我们的依赖项。

我们的 bean 的代码是：

```java
@Component
public class CircularDependencyA implements ApplicationContextAware, InitializingBean {

    private CircularDependencyB circB;

    private ApplicationContext context;

    public CircularDependencyB getCircB() {
        return circB;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        circB = context.getBean(CircularDependencyB.class);
    }

    @Override
    public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
        context = ctx;
    }
}
@Component
public class CircularDependencyB {

    private CircularDependencyA circA;

    private String message = "Hi!";

    @Autowired
    public void setCircA(CircularDependencyA circA) {
        this.circA = circA;
    }

    public String getMessage() {
        return message;
    }
}
```

同样，我们可以运行之前的测试，并看到没有抛出异常并且测试按预期工作。