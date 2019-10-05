package tech.freecode.blogsystem.controller;


import tech.freecode.blogsystem.service.VisitedTimeService;
import tech.freecode.commonmark.ext.comment.CommentNode;
import tech.freecode.commonmark.ext.comment.MetadataUtils;
import tech.freecode.commonmark.ext.visitors.LanguageVisitor;
import org.commonmark.node.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.freecode.blogsystem.service.MarkdownBlogService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

@Controller
public class MarkdownController {

    @Resource
    private MarkdownBlogService markdownBlogService;

    @Resource
    private MetadataUtils metadataUtils ;

    @Resource
    private VisitedTimeService visitedTimeService;

    @GetMapping("/**/*.html")
    public String getBlog(HttpServletRequest request, Model model){
        String path = request.getRequestURI();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {

        }

        Node document  = markdownBlogService.parseMarkdown(path);

        LanguageVisitor visitor = new LanguageVisitor();
        document.accept(visitor);
        Set<String> languages = visitor.getLanguages();
        model.addAttribute("languages",languages);

        String blogContent = markdownBlogService.generateBlogHtml(document);
        model.addAttribute("blogContent",blogContent);

        String catalog = markdownBlogService.generateCatalog(document);
        model.addAttribute("calalog",catalog);

        List<CommentNode> metadatas = metadataUtils.getCommentNodes(document);
        model.addAttribute("metadatas",metadatas);

        String docId = path.substring(1,path.toLowerCase().lastIndexOf(".html"));
        long visitedTimes = visitedTimeService.incrementAndGet(docId);
        model.addAttribute("visitedTimes",visitedTimes);

        return "blog";
    }

}