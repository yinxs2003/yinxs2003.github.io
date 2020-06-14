---
title: FastDFS集群安装
date: 2016-06-24
tags: 分布式
---
fastdfs安装和storage server的nginx配置参见 
``` bash
http://www.cnblogs.com/Yin-BloodMage/p/5433629.html
```

tracker和storage都需要安装nginx，这里说下tracker server nginx配置。创建mod_fastdfs文件，执行  
``` bash
sudo cp /usr/yKF6600/fdfs/fastdfs-nginx-module-master/src/mod_fastdfs.conf /etc/fdfs/
```
修改mod_fastdfs文件
``` bash
connect_timeout=2
network_timeout=30
base_path=/data/ngx
load_fdfs_parameters_from_tracker=true
storage_sync_file_max_delay = 86400
use_storage_id = false
storage_ids_filename = storage_ids.conf
tracker_server=172.27.12.61:22122
storage_server_port=23000
group_name=group1
url_have_group_name = true
store_path_count=1
store_path0=/data/fdfs
log_level=debug
log_filename=/data/ngx/mod_nginx.log 
response_mode=proxy
if_alias_prefix=
flv_support = true
flv_extension = flv
group_count = 0
```
配置tracker server的nginx.conf,编辑sudo vim /usr/local/openresty/nginx/conf/nginx.conf，upsstream的反向代理使用默认的80端口
```
#user  nobody;
user fastdfs;
worker_processes  1;

error_log   /data/ngx/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    
    #设置缓存参数  
    server_names_hash_bucket_size 128;  
    client_header_buffer_size 32k;  
    large_client_header_buffers 4 32k;  
    client_max_body_size 300m;  
    sendfile        on;  
    tcp_nopush     on;  
    proxy_redirect off;  
    proxy_set_header Host $http_host;  
    proxy_set_header X-Real-IP $remote_addr;  
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;  
    proxy_connect_timeout 90;  
    proxy_send_timeout 90;  
    proxy_read_timeout 90;  
    proxy_buffer_size 16k;  
    proxy_buffers 4 64k;  
    proxy_busy_buffers_size 128k;  
    proxy_temp_file_write_size 128k;  

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    #设置缓存存储路径、存储方式、分配内存大小、磁盘最大空间、缓存期限
    proxy_cache_path /data/ngx/cache/proxy_cache levels=1:2 keys_zone=http-cache:500m max_size=10g inactive=30d;  
    proxy_temp_path /data/ngx/cache/proxy_cache/tmp;  

    access_log  /data/ngx/access.log  main;

    #sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    upstream fdfs_group1 { #设置group1的服务器，使用默认的端口80
                server 172.27.12.65 weight=1 max_fails=2 fail_timeout=30s;
                server 172.27.12.63 weight=1 max_fails=2 fail_timeout=30s;
    }


    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;
    charset utf8;

    location /group1/M00 {
        proxy_next_upstream http_502 http_504 error timeout invalid_header;  
                proxy_cache http-cache;  
                proxy_cache_valid  200 304 12h;  
                proxy_cache_key $uri$is_args$args;  
                proxy_pass http://fdfs_group1;  
                expires 30d; 
        }
    }
}
```
创建ngx缓存目录
``` bash
sudo mkdir -p /data/ngx/cache/proxy_cache/tmp
sudo chown -R fastdfs:fastdfs /data/ngx/
```
重新载入nginx配置文件
``` bash
sudo /usr/local/openresty/nginx/sbin/nginx -s reload
```
参考文章
``` bash
http://blog.csdn.net/zhu_tianwei/article/details/46045641"
```
