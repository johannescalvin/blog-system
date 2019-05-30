package tech.freecode.blogsystem.domain;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

public class Blog implements Comparable<Blog> {
    private String title;
    private Date publishedDate;
    private Date modifiedDate;
    private Set<String> keywords;
    private Set<String> descriptions;
    private String url;

    public String getTitle() {
        return title;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public Set<String> getDescriptions() {
        return descriptions;
    }

    public String getUrl() {
        return url;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public void setDescriptions(Set<String> descriptions) {
        this.descriptions = descriptions;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title_zh,String title_en){
        if (title_zh != null && title_zh.trim().length() != 0){
            this.title = title_zh;
        }else {
            this.title = title_en;
        }
    }



    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blog)) return false;

        Blog blog = (Blog) o;

        return url != null ? url.equals(blog.url) : blog.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    // 使用modifiedDate而不使用oublishedDate是有原因的
    // Linux系统上,无法获得文件的创建时间
    // publishedDate的获得必须依赖markdown的元数据注释
    // 如果在此处使用,那么元数据注释将变成强制性要求
    // 这不是一个理智的程序员该做的事情
    @Override
    public int compareTo(Blog blog) {
        if (this.modifiedDate == null || blog.modifiedDate == null){
            return this.getTitle().compareTo(blog.getTitle());
        }
        // 按照修改时间降序排序
        int compare = blog.modifiedDate.compareTo(this.modifiedDate);
        if (compare == 0){
            // 因为项目打算使用git hook自动更新
            // 所以日志的修改时间是可能在毫秒级都是一致的
            // 这个时候就需要按照标题字顺排序
            // 否则其作为TreeSet元素，内容不同，但修改时间相同的两个文件会被认为是一个
            return this.getTitle().compareTo(blog.getTitle());
        }else {
            return compare;
        }
    }
}
