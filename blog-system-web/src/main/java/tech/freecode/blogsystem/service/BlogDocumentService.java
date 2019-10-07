package tech.freecode.blogsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.freecode.blogsystem.domain.BlogDocument;

public interface BlogDocumentService {

    BlogDocument add(String blogId);
    BlogDocument update(String blogId);
    BlogDocument get(String blogId);
    boolean delete(String blogId);
    Page<BlogDocument> findAll(Pageable pageable);
}
