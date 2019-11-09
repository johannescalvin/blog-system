# Get Started

一个可以使用markdown在本地写作，使用git进行同步的个人博客系统.

## 假设

为了能够快速进入正题、启动服务，我们不妨做些假设

1. 你有一台具备公网IP的Linux服务器
2. 博客服务器的`HOME`目录下没有`markdown-blog.git`,`markdown-blog`以及`blog-site-map`这三个目录
3. 博客服务器已经安装了JRE/JDK
4. 博客服务器已经安装了Git
5. 你在服务器上运行了`ElasticSearch`服务，其`cluster-name`为`elasticsearch`,访问地址为`127.0.0.1:9200`(HTTP)和`127.0.0.1:9300`(TCP)
6. 你的本地机器上也安装了Git

## 设置Git Repository

```shell
# 进入一个有权限读写的地方
cd ~
# 如果你不想费事修改配置
git --bare init markdown-blog.git
git clone "${USER}"@localhost:"${HOME}"/markdown-blog.git
cd markdown-blog.git/hooks
cp post-update.sample post-update
```

然后编辑`post-update`

```shell
#!/bin/sh
unset GIT_DIR
cd ~/markdown-blog
branch=`git branch | awk '{print $2}'`
if [ ! $branch == 'master' ];then
    echo '切换至master分支'
    git checkout master
    if [ $? -ne 0 ];then
        echo '切换至master分支失败'
        exit 1;
    fi
fi

git  pull origin master
```

**注意** 为了不至于每次`pull`操作都需要输入密码，需要将`~/.ssh/id_rsa.pub`追加至`~/.ssh/authorized_keys`中。

除了手动操作外，下面的脚本可能对你有帮助

```shell
id_rsa_pub="`cat ~/.ssh/id_rsa.pub`"
id_rsa_pub=`echo -n $id_rsa_pub | cut -c 20-50`
match_str=`cat ~/.ssh/authorized_keys | grep $id_rsa_pub`
if [ ${#match_str} -ne 0 ]; then
    echo '已添加'
else
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
fi
```

## 运行服务

```shell
java -jar blogsystem.jar
```

此时服务应该已经可以起来了，能够接收你本地push的markdown文件，并实时更新。

可以使用`curl`判断服务是否应该运行

```shell
curl http://localhost:8080/
```

如果`curl`表明服务已经正常运行，但公网仍不能访问，注意检查你的安全设置；或者使用`Nginx`进行反向代理。

## 设置本地客户端

在本地机器上克隆linux服务器的博客项目

```shell
git clone 服务器用户名@服务器域名或者公网IP:服务器HOME目录/markdown-blog.git
```

进入当前git工作目录,假定为`~/blog/markdown-blog/`好了,该目录下的`.md`文件更改只要被`push`至git服务器端，就会触发部署在同一服务上的博客系统服务，对博客进行相应更新。

## 使用shell一键完成

```shell
wget https://raw.githubusercontent.com/johannescalvin/blog-system/develop/scripts/easy-install.sh | bash
```