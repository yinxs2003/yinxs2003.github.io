---
title: url中文编码问题
date: 2016-06-15 14:44:51
tags: python
---

文章内容
<!--more-->
最近写了一个词典程序，汉译英时会报错，因为url中不能含有中文字符。最终解决方案如下

通过如下形式可以将汉字转化为可以被url识别的编码
``` python
# coding: utf-8
import urllib
import sys
string = sys.argv[1]
string = unicode(string, "gbk")
utf8_string = string.encode("utf-8")
gbk_string = string.encode("gbk")
gbk = urllib.quote(gbk_string)
utf8 = urllib.quote(utf8_string)
print gbk
print utf8
```
我上传的程序
``` python
# coding: utf-8
# http://fanyi.youdao.com/openapi.do?keyfrom=YDictionary&key=1425633743&type=data&doctype=<doctype>&version=1.1&q=hello
import simplejson as json
import urllib
import sys
reload(sys)
sys.setdefaultencoding('utf-8')


class Dict():

    def search(self, queryWord):
        url = "http://fanyi.youdao.com/openapi.do?keyfrom=" \
            "YourDictionary&key=1425633742&type=" \
            "data&doctype=" \
            "json&version=1.1&q=" + self.gbk2utf8(queryWord)
        transData = urllib.urlopen(url).read()
        trans_res = json.loads(transData)
        print '======\nBefore translation\n======\n' + trans_res["query"] + \
            '\n======\nAfter translation\n======\n' \
            + trans_res["translation"][0]

    def readArgv(self):
        queryWord = ''
        for i in range(1, len(sys.argv)):
            queryWord = queryWord + ' ' + sys.argv[i]

        return queryWord

    def gbk2utf8(self, string):
        return urllib.quote(unicode(queryWord, "gbk").encode("utf-8"))

if __name__ == '__main__':
    myDict = Dict()
    queryWord = myDict.readArgv()
    myDict.search(queryWord)
```
