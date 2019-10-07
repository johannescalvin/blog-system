package tech.freecode.blogsystem.cache;

import org.commonmark.parser.Parser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.utils.FileUtils;

import javax.annotation.Resource;
import javax.cache.annotation.*;
import javax.validation.constraints.NotNull;

@Service
//@ConditionalOnMissingBean(MarkdownDocumentCacheService.class)
public class MarkdownFileCacheServiceImpl implements MarkdownFileCacheService {
    private static final String cacheName = "markdown-doc";
    @Resource
    private Parser parser;

    @Value("${blog-system.markdown-file-base}")
    private String fileBase;

    public void setFileBase(String fileBase){
        this.fileBase = fileBase;
    }

    @Override
    @CacheResult( cacheName = "markdownFile")
    public String get(@NotNull @CacheKey String blogId) {
        return getContent(blogId);
    }

    @Override
    @CachePut(cacheName = "markdownFile")
    public void saveOrUpdate(@CacheKey String blogId, @CacheValue String markdown) {
        markdown = getContent(blogId);
    }

    @Override
    @CacheRemove(cacheName = "markdownFile")
    public void delete(String blogId) {

    }

    private String getContent(@NotNull @CacheKey String blogId){
        String filepath = fileBase+blogId+".md";
        String markdown = FileUtils.getFileContentFromDisk(filepath);
        return markdown;
    }
}
