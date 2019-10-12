#!/bin/sh

base_blog_url='https://localhost:9981'

encode_url(){
    dest_string=''
    source_string=`echo -n $1 | od -t x1 -A n | xargs`
    echo $source_string
    for str in $source_string; do
        echo $str
        if [ $str == '2f' ]; then
            dest_string=$dest_string'/'
        else
            dest_string=$dest_string'%'$str
        fi
    done
    url=$base_blog_url'/'$dest_string
}

# 最近一个pull操作发生的时间
last_pull_timestamp=`git --no-pager reflog --format="%C(auto)%H#%cD#%gs" | awk -F '#' '{if(match($3,/pull/)) print $2}' | head -1`

# --no-pager 表示不使用分页程序
# --name-status 显示每一次提交对文件的修改：A，新增；M,修改；D，删除；R100，重命名
# --since 只显示在此时间之后的日志
# tac 按行倒着输出, 这样就可以按照时间顺序对其重建

# 修改
modified_blogs=`git --no-pager log --pretty=oneline --name-status --since="$last_pull_timestamp" | grep '^M' | awk '{print $2}' | grep '.md$' | tac`
for blog in $modified_blogs ; do
    encode_url $blog
    echo '尝试索引并更新 '$url
    curl -X PUT --insecure $url
done

# 新增
added_blogs=`git --no-pager log --pretty=oneline --name-status --since="$last_pull_timestamp" | grep '^A' | awk '{print $2}' | grep '.md$' | tac`
for blog in $added_blogs ; do
    encode_url $blog
    echo '尝试添加 '$url
    curl -X POST --insecure $url
done

# 删除
added_blogs=`git --no-pager log --pretty=oneline --name-status --since="$last_pull_timestamp" | grep '^D' | awk '{print $2}' | grep '.md$' | tac`
for blog in $added_blogs ; do
    encode_url $blog
    echo '尝试删除 '$url
    curl -X DELETE --insecure $url
done

# 重命名
renamed_blogs=`git --no-pager log --pretty=oneline --name-status --since="$last_pull_timestamp" | grep '^R' | awk '{print $2,$3}' | grep '.md$' | tac`
for blog in $added_blogs ; do
    delete_blog=`echo $blog | awk '{print $1}'`
    add_blog=`echo $blog | awk '{print $2}'`
    encode_url $delete_blog
    echo '尝试删除 '$url
    curl -X DELETE --insecure $url

    encode_url $add_blog
    echo '尝试添加 '$url
    curl -X POST --insecure $url
    
done

