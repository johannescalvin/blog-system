package tech.freecode.blogsystem.service;

import tech.freecode.commonmark.ext.catalog.CatalogGenerator;
import tech.freecode.commonmark.ext.catalog.HeadingIdExtension;
import tech.freecode.commonmark.ext.catalog.internal.CatalogGeneratorImpl;
import tech.freecode.commonmark.ext.comment.CommentExtension;
import tech.freecode.commonmark.ext.comment.MetadataRenderer;
import tech.freecode.commonmark.ext.comment.internal.MetadataRendereImpl;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tech.freecode.blogsystem.utils.FileUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarkdownBlogService {

    @Value("${markdown-file-base:}")
    private String basePath ;

    List<Extension> extensions = new ArrayList<Extension>();
    private Parser parser;

    @PostConstruct
    public void init() {

        extensions.add(StrikethroughExtension.create());
        extensions.add(TablesExtension.create());
        extensions.add(AutolinkExtension.create());
        extensions.add(InsExtension.create());
        extensions.add(HeadingIdExtension.create());

        extensions.add(CommentExtension.create());

        parser = Parser.builder().extensions(extensions).build();
    }


    public Node parseMarkdown(String path)  {
        String fileName = path.substring(0,path.lastIndexOf(".html"));
        fileName = basePath + fileName + ".md";
        File file = new File(fileName);
        if (!file.exists()){
            // FIXME: 3/15/19
        }

        String markdown = FileUtils.getFileContentFromDisk(fileName);
        Node document = parser.parse(markdown);

        return document;
    }


    public String generateBlogHtml(Node document ){
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

        String html = renderer.render(document);

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
