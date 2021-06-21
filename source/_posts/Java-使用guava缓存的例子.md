---
title: Java-使用guava缓存的例子
date: 2021-04-29
toc: true
tags: Java
categories: 
- 技术
---

使用guava缓存数据源

```java
package com.code.note.test;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GuavaTest {
    private HashMap<String, String> dataSourceMap = new HashMap<>();
    private LoadingCache<String, String> localCache;

    public GuavaTest() {

        initDataSource();
        initLocalCache();
    }

    private void initLocalCache() {
        localCache = CacheBuilder.newBuilder().maximumSize(5).expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {

                    @Override
                    public String load(String key) throws Exception {
                        if (key == null) {
                            return "";
                        }
                        String value = getFromDataSource(key);
                        localCache.put(key, value);
                        return value;
                    }

                });
    }

    private String getFromDataSource(String key) {
        return dataSourceMap.get(key);
    }

    public String getFromLocalCache(String key) {
        try {
            return localCache.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initDataSource() {
        dataSourceMap.put("key1", "value1");
        dataSourceMap.put("key2", "value2");
        dataSourceMap.put("key3", "value3");
        dataSourceMap.put("key4", "value4");
    }

    public static void main(String[] args) {
        System.out.println("hello world");

        GuavaTest g = new GuavaTest();
        System.out.println(g.getFromLocalCache("key1"));
        System.out.println(g.getFromLocalCache("key2"));
    }
}
```

上面的的例子，需要使用数据源，读不到数据从数据源里读取。如果单纯使用guava当作缓存，读取不到返回null怎么实现呢？代码如下：

```java
package com.xxx.cpp.cache.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.yidian.cpp.cache.LocalCache;

public class LocalCacheImpl<K, V> implements LocalCache<K, V> {
    private static final long MAX_SIZE = 65535;

    private static final long EXPIRE_TIME = 1;

    private Cache<K, Optional<V>> caches = CacheBuilder.newBuilder().maximumSize(MAX_SIZE)
            .expireAfterAccess(EXPIRE_TIME, TimeUnit.HOURS).removalListener(new RemovalListener<K, Optional<V>>() {
                @Override
                public void onRemoval(RemovalNotification<K, Optional<V>> notification) {
                    // TODO
                }
            }).build();

    @Override
    public V get(K key) throws Exception {
        Optional<V> opt = caches.get(key, new Callable<Optional<V>>() {

            @Override
            public Optional<V> call() throws Exception {
                // TODO获取数据，加入缓存
                return Optional.fromNullable(null);
            }

        });
        return opt.isPresent() ? opt.get() : null;
    }

    @Override
    public void put(K key, V value) {
        caches.put(key, Optional.of(value));
    }

    @Override
    public void remove(Object key) {
        caches.invalidate(key);
    }

}
```

接口代码

```java
package com.xxx.cpp.cache;

public interface LocalCache<K, V> {
    /**
     * Returns the value associated with {@code key} in this cache, or
     * {@code null} if there is no cached value for {@code key}.
     **/
    V get(K key) throws Exception;

    /**
     * Associates {@code value} with {@code key} in this cache. If the cache
     * previously contained a value associated with {@code key}, the old value
     * is replaced by {@code value}.
     **/
    void put(K key, V value);

    /**
     * Discards any cached value for key {@code key}.
     */
    void remove(Object key);
}
```

测试代码

```java
package com.xxx.cpp;

import com.yidian.cpp.cache.LocalCache;
import com.yidian.cpp.cache.impl.LocalCacheImpl;

public class Test {
    public static void main(String[] args) throws Exception {
        LocalCache<String, String> localCache = new LocalCacheImpl();
        localCache.put("key","v1");
        localCache.remove("key");
        String a = localCache.get("key");
        System.out.println(a == null);
    }
}
```

