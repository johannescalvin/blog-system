package tech.freecode.blogsystem.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.commonmark.ext.FileUtils;
import tech.freecode.commonmark.ext.visitors.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

//@Service
//@Scope("prototype")
// 每次调用新建一个的实例
public class BlogExtractorService {

    private HeadingVisitor headingVisitor = new HeadingVisitor();
    private AuthorVisitor authorVisitor = new AuthorVisitor();
    private KeywordsVisitor keywordsVisitor = new KeywordsVisitor();
    private DescriptionVisitor descriptionVisitor = new DescriptionVisitor();
    private PublishedTimeVisitor publishedTimeVisitor = new PublishedTimeVisitor();
    private CodeVisitor codeVisitor = new CodeVisitor();


    private TextContentRenderer textContentRenderer;
    private String fileBase;

    public BlogExtractorService(String fileBase,TextContentRenderer textContentRenderer) {
        this.textContentRenderer = textContentRenderer;
        this.fileBase = fileBase;
    }

    private BlogDocument parse(Node markdownDocument){
        BlogDocument blogDocument = new BlogDocument();

        markdownDocument.accept(headingVisitor);
        Map<Integer, List<String>> titles = headingVisitor.getTitles();
        if (titles.containsKey(1) && titles.get(1) != null && titles.get(1).size() == 1){
            String title = titles.get(1).get(0);
            if (title != null && title.trim().length() != 0){
                blogDocument.setTitle(title);
            }
        }
        Set<String> subTitles = new LinkedHashSet<>();
        for (List<String>  list : titles.values()){
            for (String title : list){
                if (title != null && title.trim().length() != 0){
                    subTitles.add(title);
                }
            }
        }
        blogDocument.setSubTitles(subTitles);

        markdownDocument.accept(authorVisitor);
        Set<String> authors = authorVisitor.getAuthors();
        blogDocument.setAuthors(authors);

        markdownDocument.accept(keywordsVisitor);
        Set<String> keywords = keywordsVisitor.getKeywords();
        blogDocument.setKeywords(keywords);

        markdownDocument.accept(codeVisitor);
        Set<String> codes = new LinkedHashSet<String>();
        for (List<String> list : codeVisitor.getCodes().values()){
            codes.addAll(list);
        }
        blogDocument.setCodes(codes);

        String fulltext = textContentRenderer.render(markdownDocument);
        blogDocument.setFulltext(fulltext);

        markdownDocument.accept(descriptionVisitor);
        String description = descriptionVisitor.getDescription();
        blogDocument.setDescription(description);

        markdownDocument.accept(publishedTimeVisitor);
        blogDocument.setCreatedTime(publishedTimeVisitor.getCreatedTime());

        return blogDocument;
    }

    public BlogDocument parse(Parser parser,String markdownFilepath){
        if (markdownFilepath == null || (!markdownFilepath.toLowerCase().endsWith(".md"))){
            return null;
        }
        File markdownFile = new File(markdownFilepath);
        String markdown = FileUtils.getFileContentFromDisk(markdownFile);
        if (markdown == null || markdown.length() == 0){
            return null;
        }

        Node markdownDocument = parser.parse(markdown);
        BlogDocument document = parse(markdownDocument);

        Date modifiedTime = new Date(markdownFile.lastModified());
        document.setModifiedTime(modifiedTime);

        if (document.getCreatedTime() == null){
            document.setCreatedTime(modifiedTime);
        }



        String link = markdownFilepath.substring(fileBase.length(),markdownFilepath.toLowerCase().lastIndexOf(".md"));
        String id = link;
        document.setId(id);
        
        link += ".html";
        document.setLink(link);

        return document;
    }
}
