package tech.freecode.blogsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tech.freecode.blogsystem.domain.BlogDocument;

public interface BlogDocumentRepository extends ElasticsearchRepository<BlogDocument,String> {

    Page<BlogDocument> findByFulltextLike(Pageable pageable,String term);

}
