---
title: Linux挂载分区parted命令
date: 2020-06-03 10:40:30
toc: true
tags: Linux
categories: 
- 技术
---

文章内容
<!--more-->

分区命令如下

```shell
mkdir /data1

sleep 1

mkdir /data2

sleep 1



mkdir /data3
sleep 1



mkdir /data4

sleep 1



mkdir /data5

sleep 1



mkdir /data6

sleep 1



mkdir /data7

sleep 1



mkdir /data8

sleep 1







\#execute b 1



parted /dev/vdb mklabel gpt mkpart primary 0% 100%

mkfs.xfs /dev/vdb1

mount /dev/vdb1 /data1

uuid=`blkid |grep /dev/vdb|awk '{print $2}' | xargs echo`

echo $uuid" /data1 xfs defaults 1 1">>/etc/fstab







\#execute c 2

parted /dev/vdc mklabel gpt mkpart primary 0% 100%

sleep 3

mkfs.xfs /dev/vdc1



sleep 3

mount /dev/vdc1 /data2



sleep 3

uuid=`blkid |grep /dev/vdc|awk '{print $2}' | xargs echo`

echo $uuid" /data2 xfs defaults 1 1">>/etc/fstab







\#execute d 3

parted /dev/vdd mklabel gpt mkpart primary 0% 100%

sleep 3





mkfs.xfs /dev/vdd1

sleep 3



mount /dev/vdd1 /data3

sleep 3



uuid=`blkid |grep /dev/vdd|awk '{print $2}' | xargs echo`

echo $uuid" /data3 xfs defaults 1 1">>/etc/fstab







\#execute e 4



parted /dev/vde mklabel gpt mkpart primary 0% 100%

sleep 3



mkfs.xfs /dev/vde1

sleep 3



mount /dev/vde1 /data4

sleep 3



uuid=`blkid |grep /dev/vde|awk '{print $2}' | xargs echo`

echo $uuid" /data4 xfs defaults 1 1">>/etc/fstab





\#execute f 5



parted /dev/vdf mklabel gpt mkpart primary 0% 100%

sleep 3



mkfs.xfs /dev/vdf1

sleep 3



mount /dev/vdf1 /data5

sleep 3



uuid=`blkid |grep /dev/vde|awk '{print $2}' | xargs echo`

echo $uuid" /data5 xfs defaults 1 1">>/etc/fstab





\#execute g 6



parted /dev/vdg mklabel gpt mkpart primary 0% 100%

sleep 3



mkfs.xfs /dev/vdg1

sleep 3



mount /dev/vdg1 /data6

sleep 3





uuid=`blkid |grep /dev/vdg|awk '{print $2}' | xargs echo`

echo $uuid" /data6 xfs defaults 1 1">>/etc/fstab







\#execute h 7



parted /dev/vdh mklabel gpt mkpart primary 0% 100%

sleep 3



mkfs.xfs /dev/vdh1

sleep 3



mount /dev/vdh1 /data7

sleep 3



uuid=`blkid |grep /dev/vdh|awk '{print $2}' | xargs echo`

echo $uuid" /data7 xfs defaults 1 1">>/etc/fstab





\#execute i 8



parted /dev/vdi mklabel gpt mkpart primary 0% 100%

sleep 3



mkfs.xfs /dev/vdi1

sleep 3



mount /dev/vdi1 /data8

sleep 3



uuid=`blkid |grep /dev/vdi|awk '{print $2}' | xargs echo`

echo $uuid" /data8 xfs defaults 1 1">>/etc/fstab





\#execute j 9



parted /dev/vdj mklabel gpt mkpart primary 0% 100%

sleep 3



mkfs.xfs /dev/vdij

sleep 3



mount /dev/vdij /data

sleep 3



uuid=`blkid |grep /dev/vdj|awk '{print $2}' | xargs echo`

echo $uuid" /data xfs defaults 1 1">>/etc/fstab
```



