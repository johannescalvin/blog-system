package tech.freecode.blogsystem.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.cache.MarkdownFileCacheService;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.blogsystem.repository.BlogDocumentRepository;
import tech.freecode.blogsystem.utils.FileUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Optional;

@Service
public class BlogDocumentServiceImpl implements BlogDocumentService{
    @Resource
    private BlogDocumentRepository blogDocumentRepository;

    @Resource
    private Parser parser;

    @Resource
    private MarkdownFileCacheService markdownFileCacheService;

    @Value("${blog-system.markdown-file-base}")
    private String fileBase;

    @Resource
    private TextContentRenderer textContentRenderer;

    @Resource
    private VisitedTimeService visitedTimeService;

    public BlogDocument save(String blodId){

        String filepath = fileBase+blodId+".md";
        File file = new File(filepath);
        if (!file.exists() || !file.canRead()){
            return null;
        }

        BlogExtractorService blogExtractorService = new BlogExtractorService(fileBase,textContentRenderer);

        // 优先从缓存中查找，缓存不命中，再读磁盘解析
        String markdown = markdownFileCacheService.get(blodId);

        Node markdownDocument = parser.parse(markdown);

        BlogDocument document = blogExtractorService.parse(markdownDocument,filepath);
        if (document == null){
            return null;
        }
        
        return blogDocumentRepository.save(document);
    }

    @Override
    public boolean delete(String blogId){
        // 清理缓存
        markdownFileCacheService.delete(blogId);

        Optional<BlogDocument> optionalBlogDocument = blogDocumentRepository.findById(blogId);
        if (!optionalBlogDocument.isPresent()){
            return true;
        }

        blogDocumentRepository.delete(optionalBlogDocument.get());

        optionalBlogDocument = blogDocumentRepository.findById(blogId);

        return optionalBlogDocument.isPresent();


    }

    @Override
    public BlogDocument update(String blogId){
        // 更新缓存
        markdownFileCacheService.saveOrUpdate(blogId,FileUtils.getFileContentFromDisk(fileBase+blogId+".md"));
        return save(blogId);
    }

    @Override
    public BlogDocument add(String blogId){
        // 更新缓存
        markdownFileCacheService.saveOrUpdate(blogId,
                FileUtils.getFileContentFromDisk(fileBase+blogId+".md"));
        return save(blogId);
    }

    @Override
    public BlogDocument get(String blogId){
        Optional<BlogDocument> blogDocumentOptional = blogDocumentRepository.findById(blogId);
        return blogDocumentOptional.isPresent() ? blogDocumentOptional.get() : null;
    }

    @Override
    public Page<BlogDocument> findAll(Pageable pageable){
        Page<BlogDocument> results = blogDocumentRepository.findAll(pageable);
        for (BlogDocument document : results.getContent()){
            long times = visitedTimeService.getVisitedTimes(document.getId());
            document.setVisitedTimes(times);
        }
        return results;
    }
}
