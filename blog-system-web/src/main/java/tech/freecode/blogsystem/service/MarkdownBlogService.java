package tech.freecode.blogsystem.service;

import tech.freecode.blogsystem.cache.MarkdownFileCacheService;
import tech.freecode.commonmark.ext.catalog.CatalogGenerator;
import tech.freecode.commonmark.ext.catalog.internal.CatalogGeneratorImpl;
import tech.freecode.commonmark.ext.comment.MetadataRenderer;
import tech.freecode.commonmark.ext.comment.internal.MetadataRendereImpl;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MarkdownBlogService {

    @Value("${blog-system.markdown-file-base}")
    private String basePath ;

    @Resource
    private Parser parser;
    @Resource
    private HtmlRenderer htmlRenderer;

    @Resource
    private MarkdownFileCacheService markdownFileCacheService;

    public Node parseMarkdown(String blogId)  {
        String markdown = markdownFileCacheService.get(blogId);
        return parser.parse(markdown);
    }


    public String generateBlogHtml(Node document ){
        String html = htmlRenderer.render(document);
        return html;
    }

    public String generateMeta(Node document){
        MetadataRenderer metadataRenderer = new MetadataRendereImpl();
        String meta =  metadataRenderer.render(document);
        return meta;
    }

    public String generateCatalog(Node document){
        CatalogGenerator catalogGenerator = new CatalogGeneratorImpl("id","","");
        String catalog = catalogGenerator.getCatalog(document);
        return catalog;
    }

    public String generateSubLinks(Node document){
        return null;
    }
}
