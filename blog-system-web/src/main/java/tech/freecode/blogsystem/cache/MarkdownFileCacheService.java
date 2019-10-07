package tech.freecode.blogsystem.cache;

public interface MarkdownFileCacheService {
    String get(String blogId);
    void saveOrUpdate(String blogId, String markdown);
    void delete(String blogId);
}
