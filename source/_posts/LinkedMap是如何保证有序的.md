---
title: 常见OOM及原因分析
date: 2020-07-06 10:40:30
toc: true
tags: jvm
categories: 
- 技术
---

### LinkedMap为什么是有序Map

LinkedMap底层存储是数组，初始大小为16

LinkedMap返回的set是有序的，是因为KeySet中的iterator是有序的

LinkedMap在put时候，使用for循环，且起始位置用hashIndex算出来的

```java
protected int hashIndex(int hashCode, int dataSize) {
        return hashCode & dataSize - 1;
}

public Object put(Object key, Object value) {
        key = this.convertKey(key);
        int hashCode = this.hash(key);
        int index = this.hashIndex(hashCode, this.data.length);

        for(AbstractHashedMap.HashEntry entry = this.data[index]; entry != null; entry = 	   entry.next) {
            if (entry.hashCode == hashCode && this.isEqualKey(key, entry.key)) {
                Object oldValue = entry.getValue();
                this.updateEntry(entry, value);
                return oldValue;
            }
        }

        this.addMapping(index, hashCode, key, value);
        return null;
}
```

