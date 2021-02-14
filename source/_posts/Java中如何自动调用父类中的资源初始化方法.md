---
title: Java中如何自动调用父类中的资源初始化方法
date: 2020-10-20 10:40:30
toc: true
tags: 技术Java
categories: 
- 技术
---

# 一个简单的例子

## 背景

在写程序时候常常有一些资源初始化方法，我们希望这些方法能够被自动调用 ，可以使用如下方式实现自动调用

## 代码

父类代码如下：

```java
package com.bd.autocall;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Super {

    public abstract void call0();

    public void call(){
        // 在这里调用子类中的call0方法
        this.call0();
    }
}
```

子类代码如下：

```java
package com.bd.autocall;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sub extends Super {
    @Override
    public void call0() {
        // 这里写被初始化的方法
        log.info("son call0");
    }

    public static void main(String[] args) {
        Sub a = new Sub();
        a.call();
    }
}

```



执行结果如下：

```
1 [main] INFO com.bd.autocall.Sub - son call0
```

其中，call方法是对外方法，会被其他人调用

其他人调用父类的call方法的时，子类的call0方法也会被调用，从而实现call0方法被自动调用



# 复杂例子

## 背景

在Flink中，自定义一个RichFunction，其中open函数里是初始化缓存或者数据资源的方法，该方法会被Flink框架自动调用且只调用一次。我们希望在实现子类时候，即能继承open函数原有的资源，又能向open函数中添加新的资源初始化

## 代码

```java
public abstract class AbstractRichFunction implements RichFunction, AthenaCodisInterface, AthenaHbaseInterface, AthenaMediaJdbcInterface, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRichFunction.class);

    private static final long serialVersionUID = 1L;

    private External external;

    private transient CodisExternalClient<String, MediaMessage> mediaCodisClient;
    private transient CodisExternalClient<String, PostMessage> postCodisClient;
    private transient CodisExternalClient<String, Long> shunCodisClient;

    private transient HbaseExternalClient<String, PostMessage> postHbaseClient;

    private transient JdbcExternalClient<String, MediaPostEventMessage> weMediaJdbcClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        ParameterTool parameterTool = (ParameterTool)getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        String athenaConfig = parameterTool.get(ATHENA_CONFIG_NAME);
        this.external = JSONObject.parseObject(athenaConfig, External.class);

        this.open0(parameters);
    }

    public abstract void open0(Configuration parameters) throws Exception;
  
    // --------------------------------------------------------------------------------------------
    //  Runtime context access
    // --------------------------------------------------------------------------------------------

    private transient RuntimeContext runtimeContext;

    @Override
    public void setRuntimeContext(RuntimeContext t) {
        this.runtimeContext = t;
    }

    @Override
    public RuntimeContext getRuntimeContext() {
        if (this.runtimeContext != null) {
            return this.runtimeContext;
        } else {
            throw new IllegalStateException("The runtime context has not been initialized.");
        }
    }

    @Override
    public IterationRuntimeContext getIterationRuntimeContext() {
        if (this.runtimeContext == null) {
            throw new IllegalStateException("The runtime context has not been initialized.");
        } else if (this.runtimeContext instanceof IterationRuntimeContext) {
            return (IterationRuntimeContext) this.runtimeContext;
        } else {
            throw new IllegalStateException("This stub is not part of an iteration step function.");
        }
    }

    // --------------------------------------------------------------------------------------------
    //  Default life cycle methods
    // --------------------------------------------------------------------------------------------

    @Override
    public void close() throws Exception {}

    @Override
    public synchronized CodisExternalClient<String, MediaMessage> getMediaCodisClient() throws Exception {

        if(mediaCodisClient != null) return mediaCodisClient;
        Map<AthenaExternalEnum, CodisExternal> map = external.getCodis();
        CodisExternal codisExternal = map.get(AthenaExternalEnum.MEDIA_CACHE_EVENT);
        mediaCodisClient = codisExternal.getClient();
        return mediaCodisClient;

    }

    @Override
    public synchronized CodisExternalClient<String, PostMessage> getPostCodisClient() throws Exception {

        if(postCodisClient != null) return postCodisClient;
        Map<AthenaExternalEnum, CodisExternal> map = external.getCodis();
        CodisExternal codisExternal = map.get(AthenaExternalEnum.POST_CACHE_EVENT);
        postCodisClient = codisExternal.getClient();
        return postCodisClient;

    }

    @Override
    public synchronized CodisExternalClient<String, Long> getShunCodisClient() throws Exception {
        if(shunCodisClient != null) return shunCodisClient;
        Map<AthenaExternalEnum, CodisExternal> map = external.getCodis();
        CodisExternal codisExternal = map.get(AthenaExternalEnum.SHUN_CACHE_EVENT);
        shunCodisClient = codisExternal.getClient();
        return shunCodisClient;

    }

    @Override
    public synchronized JdbcExternalClient<String, MediaPostEventMessage> getWeMediaJdbcClient() throws Exception {
        if(weMediaJdbcClient != null) return weMediaJdbcClient;
        Map<AthenaExternalEnum, JdbcExternal> map = external.getJdbc();
        JdbcExternal jdbcExternal = map.get(AthenaExternalEnum.WEMEDIA_MYSQL_EVENT);
        return jdbcExternal.getClient();

    }

    @Override
    public synchronized HbaseExternalClient<String, PostMessage> getPostHbaseClient() throws Exception {
        return external.getHbase().get(AthenaExternalEnum.POST_HBASE_EVENT).getClient();
    }

    @Override
    public HbaseExternalClient<String, MediaMessage> getMediaHbaseClient() throws Exception {
        return external.getHbase().get(AthenaExternalEnum.MEDIA_HBASE_EVENT).getClient();
    }
}
```

子类

```java
public abstract class RichFlatMapFunction<IN, OUT> extends AbstractRichFunction implements FlatMapFunction<IN, OUT> {
    @Override
    public abstract void flatMap(IN value, Collector<OUT> out) throws Exception;

}
```

子类2

```java
public class PostMapFunction extends RichFlatMapFunction<PostFactEventMessage, PostFactEventMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(PostFactMsgMapFunction.class);

    private transient HbaseExternalClient<String, PostMessage> postHbaseClient;
    private transient LoadingCache<String, PostMessage> loadingCache;
    private transient static final AtomicLong codisCount = new AtomicLong(0);
    private transient static final AtomicLong hbaseCount = new AtomicLong(0);


    @Override
    public void open0(Configuration parameters) throws Exception {
        postHbaseClient = getPostHbaseClient();

        loadingCache = CacheBuilder.newBuilder()
                .maximumSize(5000000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, PostMessage>() {
                    @Override
                    public PostMessage load(String docId) throws IOException {
                        PostMessage postMessage = null;
                        try {
                            postMessage = getPostCodisClient().getObject(docId);
                        } catch (Exception e) {
                            LOG.warn("codis error", e);
                        }
                        LOG.error("load postMessage from codis count={}", codisCount.addAndGet(1));
                        if (Objects.isNull(postMessage) && StringUtils.isNotBlank(docId)) {
                            postMessage = postHbaseClient.getObject(docId);
                            LOG.error("load postMessage from hbase count={}", hbaseCount.addAndGet(1));
                            LOG.error("load postMessage from hbase finish");
                        }
                        if (Objects.isNull(postMessage)) {
                            postMessage = new PostMessage();
                            postMessage.setNewsId(docId);
                        }
                        return postMessage;
                    }
                });

    }

    @Override
    public void flatMap(PostFactEventMessage value, Collector<PostFactEventMessage> out) throws Exception {

        PostMessage postMessage = loadingCache.get(value.getDocId());
        if (Objects.isNull(postMessage)) {
            LOG.error("postMessage=null, docId = {}", value.getDocId());
            return;
        }
        if(postMessage.getMediaId() == null) return;

        value.loadPostMessage(postMessage);
        out.collect(value);

    }


}
```

从代码中可以看出，PostMapFunction类在open0中初始化了postHbaseClient资源，并包装成guava缓存读取器。由于open0被父类调用，postHbaseClient会被加载到open函数里，并被flink作为分布式资源使用

