#!/bin/bash

cd ~
# 如果你不想费事修改配置
# git --bare init markdown-blog.git
# git clone "${USER}"@localhost:"${HOME}"/markdown-blog.git
cd ~/markdown-blog.git/hooks

cat <<EOF >  ~/markdown-blog.git/hooks/post-update
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
EOF

chmod +x post-update

# 插件~/.ssh/id_rsa.pub是否在~/.ssh/authorized_keys中
id_rsa_pub="`cat ~/.ssh/id_rsa.pub`"
id_rsa_pub=`echo -n $id_rsa_pub | cut -c 20-50`
match_str=`cat ~/.ssh/authorized_keys | grep $id_rsa_pub`
if [ ${#match_str} -ne 0 ]; then
    echo '已添加'
else
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
fi
