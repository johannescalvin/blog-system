package tech.freecode.blogsystem.controller;


import org.atlassian.commonmark.ext.comment.CommentNode;
import org.atlassian.commonmark.ext.comment.MetadataUtils;
import org.atlassian.commonmark.ext.languages.LanguageVisitor;
import org.commonmark.node.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.freecode.blogsystem.service.BlogInitService;
import tech.freecode.blogsystem.service.MarkdownBlogService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

@Controller
public class MarkdownController {

    @Value("${markdown-file-base:}")
    private String basePath;

    @Resource
    private BlogInitService blogInitService;
    @Resource
    private MarkdownBlogService markdownBlogService;

    @Resource
    private MetadataUtils metadataUtils ;


    @GetMapping("/blog/**/*.html")
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

        return "blog";
    }


    @GetMapping("/test/**.html")
    @ResponseBody
    public String getHtml(HttpServletRequest request, HttpServletResponse response) {
        return "html";
    }

    @GetMapping("/test/**.png")
    @ResponseBody
    public String getPng(HttpServletRequest request, HttpServletResponse response) {
        return "png";
    }

    @PutMapping("/yangchengzhu/**.html")
    public void update(HttpServletRequest request) {
        String path = request.getRequestURI().substring(1);
        blogInitService.updateBlog(path);
    }


}