# 配置文件

为了便于不使用IDE的用户查看和编辑，使用`.properties`文件，而不使用`.yaml`。如有需要，请自行转换。

```shell
java -jar -Dspring.config.location=D:\config\config.properties springbootrestdemo-0.0.1-SNAPSHOT.jar
```

## 必须包含的配置项

```shell
# 系统管理员账号和密码
# 强烈建议修改，否则将有安全隐患！
blog-system.blog-document-crud-admin.user-name=
blog-system.blog-document-crud-admin.user-password=

# 必须设置的前端展示项
# 否则采用默认值，需要博客系统可以运行
# 但是会展示无意义的信息

# 网站首页标题
blog-system.index-page.display-title=
# 网站首页meta标签中的中文关键词
blog-system.index-page.seo-keywords-zh=
# 网站首页meta标签中的英文关键词
blog-system.index-page.seo-keywords-en=
# 网站用途描述
blog-system.index-page.seo-descriptions=
# 网站公开域名必须展示的备案号
blog-system.index-page.recode-code=
# 联系方式信息
blog-system.index-page.contact=

# 网站地图
# 博客地址
blog-system.seo.site-map.server-url=
```

## markdown-blog位置设置

系统默认监控`${HOME}/markdown-blog`目录下的`.md`文件变化。该目录不应该中的文件，不应该有任何手动更改。其更改应该全部来源于`git pull`操作。该目录下也不应该`git checkout branch`之类会导致文件变动的操作。

```shell
blog-system.markdown-file-base=
```

修改该项设置，会导致[Get Started](get-started.md)中的脚本不能直接可用。所以，如果可能的话，尽量不要修改本项配置。

## 配置端口

### 同时开启HTTP和HTTPS端口

系统默认开启`HTTPS`和`HTTP`协议，分别运行在`8443`和`8080`端口。主要是用户习惯不输入`https://`,所以系统强制将`HTTP`的访问重定向至`HTTPS`。

同时开启`HTTP`和`HTTPS`将消耗一定的服务器资源。同时，在没有`HTTPS`证书的情况下，强制使用`HTTPS`访问，浏览器会阻止第一次访问的用户，总是感觉少了点意思。如果希望访问者能有更好的体验，最好去申请一起免费的`HTTPS`证书（土豪随意）。

相关配置如下

```shell
server.port=
server.ssl.enabled=true
server.ssl.key-alias=tomcat
server.ssl.key-password=
server.ssl.key-store-type=JKS
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=

http.with-https.enabled=true
http.port=
```

这是系统默认使用配置，特定拧出来说，主要是证书不应该使用我提供的默认生成版本，如果有购买的商业证书自然好。没有的话，可以使用`keygen`生成一个

```shell
keytool -genkey -alias tomcat -dname "CN=Andy,OU=kfit,O=kfit,L=HaiDian,ST=BeiJing,C=CN" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 365 -keypass 123456 -storepass 123456
```

注意指定`-keypass`和`-storepass`的值，并分别设置到配置项`server.ssl.key-password`和`server.ssl.key-store-password`中。

### 不强制使用HTTPS

```shell
http.force-redirect-to-https.enabled=false
```

此时，用户通过`HTTP`和`HTTPS`都能访问我们的网站。但由于不会强制用户使用`HTTPS`，所以即使在你没有`HTTPS`证书的情况下，用户通过`HTTP`的访问请求，浏览器不会阻止。

如果在安全要求比较高的场景中，这么干是不合适，好想对于我们的个人博客来讲，如果没有设置`blog-system.markdown-file-watch-service.enabled=false`,全站只有`GET`请求，好像也没啥影响。

### 只开启HTTPS端口

如果想用户只能需要`HTTPS`访问你的博客，那么你可以关闭`HTTP`端口。

```shell
# HTTPS相关设置
server.port=
server.ssl.enabled=true
server.ssl.key-alias=tomcat
server.ssl.key-password=
server.ssl.key-store-type=JKS
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=

# 关闭HTTP端口
http.with-https.enabled=false
```

**注意**，用户都是懒的，在浏览器输入地址不太可能带协议。如果不想用户在浏览器地址栏键入http地址时报错，那么你需要一个`Nginx`反向代理，在其中实现`HTTP`至`HTTPS`的强制调转。具体方法可以参见[nginx强制使用https访问](https://blog.csdn.net/wzy_1988/article/details/8549290)。

### 只开启HTTP端口

如果你不想开启`HTTPS`，或者，如果你的博客服务运行在开启了`HTTPS`支持的`Nginx`反向代理之后，那么可以只开启HTTP端口。

```shell
server.port=
server.ssl.enabled=false
http.with-https.enabled=false
```

## 配置Elasticsearch

如果`Elasticsearch`的`cluster-name`不是`elasticsearch`，其TCP服务地址不是`127.0.0.1:9300`,可以通过下面的配置项进行更改。

```shell
spring.data.elasticsearch.cluster-name=
spring.data.elasticsearch.cluster-nodes=
```

**注意** 一定不要在公网环境下暴露自己无密码保护的Elasticsearch实例。

## FileWatch Service

系统默认使用`FileWatch Service`监控`.md`的文件变化情况。但由于Java的`FileWatch Service`虽然利用了操作系统的事件通知机制，但没有回调机制。只能单独开启一个线程等待文件更新事件的到来。虽然在没有事件时，线程被阻塞会让出CPU，但总感觉有点难受。另外，如果博客数量和博客的目录层级很多的话，很可能因为注册过多事件到操作系统，消耗内存。

尽管在个人博客系统中不太可能遇到这种情况。但以防万一，系统还通过git钩子实现了另一种博客更新机制。为减少内存消耗，通过git更新博客，与`FileWatch Service`都是排他的。

```shell
blog-system.markdown-file-watch-service.enabled=false
```

## 博客访问次数统计

为了能够对每篇博客按照热度排序，需要统计每篇博客的访问次数，如果每增加一个访问，就将其写回elasticsearch,无疑是不合适。我设计了一个缓存。当页面被访问时，其访问计数加1，但只有其访问次数的上次写回elasticsearch的时间距离现在已经超过给定时长之后，缓存的访问计数才会被**异步**地更新至elasticsearch。

这个时间以秒为单位，可以根据自己博客地数量以及elasticsearch实例的负载灵活设置。比如半小时(`1800`),一小时(`3600`),半天(`43200`),一天(`86400`)。

一般地，为了elasticsearch返回的按热度排序的搜索结果能够更好的反映实际情况，该值不应该超过`86400`太多。

```shell
blog-system.page-view-times.sync-duration-seconds-threshold=86400
```

## 链接有效性检测

系统默认不检测所有博客中的内外链接的有效性,这将给博客的质量带来隐患。

系统允许用户通过`cron`脚本指定链接有效性检测行为的发生时间和频率。比如，指定每天凌晨运行链接有效性检测相关任务。

```shell
blog-system.link-accessibility-validator.enabled=true
# 示例
blog-system.link-accessibility-validator.cron="0 0 0 * * ?"
```

此时，可以通过`/admin/link-accessibility-validation-reports`查看链接有效性检测的结果。

## Site Map相关设置

```shell
# site-map的存放位置
# 默认为 ${HOME}/blog-site-map/
blog-system.seo.site-map.store-dir=
# 博客服务器的公网IP
blog-system.seo.site-map.server-url=
# 更新时间和频率设置
# 示例: 每天凌晨更新site-map
blog-system.seo.site-map.server-url="0 0 0 * * ?"
```

## 配置文件缓存

为了加速对`.md`文件的访问，系统使用`Ehcache 3.6.2`实现了对`.md`文件的缓存。由于不知道每个博客服务器的配置情况，文件缓存配置得很粗造。

系统提供更改配置地方法...待续。
