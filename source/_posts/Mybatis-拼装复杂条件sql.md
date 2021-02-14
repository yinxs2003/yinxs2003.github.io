---
title: Mybatis拼装复杂条件sql
date: 2021-01-27
toc: true
tags: 技术Java
categories: 
- 技术
---

## 需求背景

因为mybatis不好拼装如下sql

```sql
select doc_id,encourage_card_id 
from encourage_card_quantitative_fake_progress 
where (doc_id = 'a' and encourage_card_id = 'b') or  (doc_id = 'c' and encourage_card_id = 'd') 
```

<!--more-->

所以我们在编写sql是可以使用concat函数拼装column来完成上面的sql的效果，sql如下：

```sql
SELECT *
FROM encourage_card_quantitative_fake_progress
WHERE (concat(doc_id,'_',encourage_card_id) in ('1jshdfjka_12345','2jshdfjka_2'))
```

在mybatis中实现上面sql代码如下：

```JAVA
@Component
public interface EncourageCardQuantitativeFakeProgressMapper {

    @SelectProvider(type = EncourageCardQuantitativeFakeProgressBuilder.class, method = "buildListByCondition")
    List<EncourageCardQuantitativeFakeProgress> listByCondition(@Param("postDatas") List<ProgressBarReq.PostData> postDatas);

    class EncourageCardQuantitativeFakeProgressBuilder implements ProviderMethodResolver {

        public static String buildListByCondition(Map<String, Object> parameters) {
            List<ProgressBarReq.PostData> postDatas = (List<ProgressBarReq.PostData>) parameters.get("docRelationIds");
            final SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM(MysqlTable.encourage_card_quantitative_fake_progress.name());

            List<String> strArr = new ArrayList<>();
            for (ProgressBarReq.PostData postData : postDatas) {
                strArr.add("'" + postData.getDocId() + "_" + postData.getEncourageCardId() + "'");
            }

            sql.WHERE("concat(doc_id,'_',encourage_card_id) in (" + StringUtils.join(strArr, ",") + ")");
            return sql.toString();
        }
    }
}
```

其中需要注意的是绑定类的代码形参是

Map<String, Object> parameters

如果想获取传入的List参数需要增加如下代码

List<ProgressBarReq.PostData> postDatas = (List<ProgressBarReq.PostData>) parameters.get("docRelationIds");