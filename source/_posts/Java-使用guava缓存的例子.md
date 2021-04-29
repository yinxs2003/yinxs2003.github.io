---
title: Java-使用guava缓存的例子
date: 2021-04-29
toc: true
tags: Java
categories: 
- 技术
---

使用guava缓存的例子

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

