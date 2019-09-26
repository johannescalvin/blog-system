package tech.freecode.blogsystem.service;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.blogsystem.repository.BlogDocumentRepository;

import javax.annotation.Resource;
import java.io.File;

@Service
public class BlogDocumentService {
    @Resource
    private BlogDocumentRepository blogDocumentRepository;

    @Resource
    private Parser parser;

    @Value("${blog-system.markdown-file-base}")
    private String fileBase;

    @Resource
    private TextContentRenderer textContentRenderer;

    @Resource
    private VisitedTimeService visitedTimeService;


    public BlogDocument save(String filepath){
        File file = new File(filepath);
        if (!file.exists() || !file.canRead()){
            return null;
        }

        BlogExtractorService blogExtractorService = new BlogExtractorService(fileBase,textContentRenderer);

        BlogDocument document = blogExtractorService.parse(parser,filepath);
        if (document == null){
            return null;
        }
        
        return blogDocumentRepository.save(document);
    }

    public Page<BlogDocument> findAll(Pageable pageable){
        Page<BlogDocument> results = blogDocumentRepository.findAll(pageable);
        for (BlogDocument document : results.getContent()){
            long times = visitedTimeService.getVisitedTimes(document.getId());
            document.setVisitedTimes(times);
        }
        return results;
    }
}
