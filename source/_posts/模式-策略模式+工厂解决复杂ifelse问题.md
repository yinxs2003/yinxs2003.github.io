---
title: 模式-策略模式+工厂解决复杂ifelse过多问题
date: 2021-01-27
toc: true
tags: Java
categories: 
- 技术
---

## 需求背景

1.按小时、天查询不同表

2.按照特定领域、全领域按照不同字段排序

3.按照文章类型进行排序

因此条件很多，需要多个if条件判断

```JAVA
private List<MysqlBillboardCateTopviewDoc> getHourOrDayDocs(String cate, BillboardScheduleReq.PostType postType, BillboardScheduleReq.SortType sortType) {
        List<MysqlBillboardCateTopviewDoc> hourOrDayDocs;
        if (sortType == BillboardScheduleReq.SortType.hour) {

            if (Strings.isBlank(cate)) {// 全领域
                hourOrDayDocs = mysqlBillboardCateTopviewDocHourlyService.listAllCatePostTypeHourly(postType.getValue());
            } else { //分领域
                hourOrDayDocs = mysqlBillboardCateTopviewDocHourlyService.listTypeCatePostTypeHourly(cate, postType.getValue());
            }

        } else { // 天排行榜

            if (Strings.isBlank(cate)) { // 全领域
                hourOrDayDocs = mysqlBillboardCateTopviewDocDailyService.listAllCatePostTypeDaily(postType.getValue());
            } else { //分领域
                hourOrDayDocs = mysqlBillboardCateTopviewDocDailyService.listTypeCatePostTypeDaily(cate, postType.getValue());
            }
        }
        return hourOrDayDocs;
    }


```

<!--more-->

## 调用接口

使用策略模式加工厂模式能够很好的解决该问题，使用设计模式修改后的代码，直接根据传入的类型获取结果

```java
private List<MysqlBillboardCateTopviewDoc> getHourOrDayDocs(String cate, BillboardEnum.PostType postType, BillboardEnum.SortType sortType) {
    String key;
    if (Strings.isNotBlank(cate)) {
        key = CateStrategyFactory.buildKey("type", sortType);
    } else {
        key = CateStrategyFactory.buildKey("", sortType);
    }
    Strategy strategy = CateStrategyFactory.get(key);
    return strategy.getViewDocs(cate, postType);
}
```



## 具体实现

定义了一个策略接口

```java
public interface Strategy {
    List<MysqlBillboardCateTopviewDoc> getViewDocs(String cate, BillboardEnum.PostType postType);
}
```

定义一个生产策略的工厂

```java
public class CateStrategyFactory {

    private CateStrategyFactory() {
    }

    private static Map<String, Strategy> services = new ConcurrentHashMap<>();

    public static Strategy get(String key) {
        return services.get(key);
    }

    public static String buildKey(String cate, BillboardEnum.SortType sortType) {
        return "sortType_" + sortType + CommonValue.STR_SEMICOLON + "cate_" + cate;
    }

    public static void register(String key, Strategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("strategy can't be null");
        }
        services.put(key, strategy);
    }
}
```



TypeCateHourStrategy小时细分领域策略类

```java
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeCateHourStrategy implements Strategy, InitializingBean {

    @Autowired
    private MysqlBillboardCateTopviewDocHourlyService mysqlBillboardCateTopviewDocHourlyService;

    @Override
    public List<MysqlBillboardCateTopviewDoc> getViewDocs(String cate, BillboardEnum.PostType postType) {
        return mysqlBillboardCateTopviewDocHourlyService.listTypeCatePostTypeHourly(cate, postType.getValue());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String cate = "type"; // 特定领域
        BillboardEnum.SortType sortType = BillboardEnum.SortType.hour;
        String key = CateStrategyFactory.buildKey(cate, sortType);
        CateStrategyFactory.register(key, this);
    }
}

```

TypeCateDayStrategy日细分领域策略

```java
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeCateDayStrategy implements Strategy, InitializingBean {

    @Autowired
    private MysqlBillboardCateTopviewDocDailyService mysqlBillboardCateTopviewDocDailyService;

    @Override
    public List<MysqlBillboardCateTopviewDoc> getViewDocs(String cate, BillboardEnum.PostType postType) {
        return mysqlBillboardCateTopviewDocDailyService.listTypeCatePostTypeDaily(cate, postType.getValue());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String cate = "type"; // 特定领域
        BillboardEnum.SortType sortType = BillboardEnum.SortType.day;
        String key = CateStrategyFactory.buildKey(cate, sortType);
        CateStrategyFactory.register(key, this);
    }
}
```

AllCateHourStrategy全领域小时策略

```java
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllCateHourStrategy implements Strategy, InitializingBean {

    @Autowired
    private MysqlBillboardCateTopviewDocHourlyService mysqlBillboardCateTopviewDocHourlyService;

    @Override
    public List<MysqlBillboardCateTopviewDoc> getViewDocs(String cate, BillboardEnum.PostType postType) {
        return mysqlBillboardCateTopviewDocHourlyService.listAllCatePostTypeHourly(postType.getValue());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String cate = ""; // 全领域
        BillboardEnum.SortType sortType = BillboardEnum.SortType.hour;
        String key = CateStrategyFactory.buildKey(cate, sortType);
        CateStrategyFactory.register(key, this);
    }
}
```

AllCateDayStrategy全领域日策略

```java
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 所有领域天排行策略
 */
@Component
public class AllCateDayStrategy implements Strategy, InitializingBean {

    @Autowired
    private MysqlBillboardCateTopviewDocDailyService mysqlBillboardCateTopviewDocDailyService;

    @Override
    public List<MysqlBillboardCateTopviewDoc> getViewDocs(String cate, BillboardEnum.PostType postType) {
        return mysqlBillboardCateTopviewDocDailyService.listAllCatePostTypeDaily(postType.getValue());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String cate = ""; // 全领域
        BillboardEnum.SortType sortType = BillboardEnum.SortType.day;
        String key = CateStrategyFactory.buildKey(cate, sortType);
        CateStrategyFactory.register(key, this);
    }
}
```

