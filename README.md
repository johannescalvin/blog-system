# blog-system

## 安装

创建配置文件
一个可以使用markdown在本地写作，使用git上传的个人博客系统

```shell
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.2.2
```

```shell
keytool -genkey -alias tomcat -dname "CN=Andy,OU=kfit,O=kfit,L=HaiDian,ST=BeiJing,C=CN" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 365 -keypass 123456 -storepass 123456
```

## 编写博客

### 插入Latex公式

本系统能够自动检测到markdown文本中的latex公式，你只需要在markdown中插入

<pre>
```latex
\sum _{i = 0}^{10} (i^{2/3})= 25
```
</pre>

以上代码会自动被渲染为
![latex.png](images/latex.png)
