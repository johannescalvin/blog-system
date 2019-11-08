package tech.freecode.blogsystem.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.blogsystem.service.BlogDocumentServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@ConditionalOnProperty(name = "blog-system.markdown-file-watch-service.enabled",havingValue = "false")
public class BlogDocumentController {
    @Resource
    private BlogDocumentServiceImpl blogDocumentService;
    @PutMapping("/**/*.md")
    @Secured("ROLE_ADMIN")
    public void update(HttpServletRequest request) {
        String blogId = getBlogId(request);
        blogDocumentService.update(blogId);
    }

    @DeleteMapping("/**/*.md")
    @Secured("ROLE_ADMIN")
    public boolean delete(HttpServletRequest request){
        String blogId = getBlogId(request);
        return blogDocumentService.delete(blogId);
    }

    @PostMapping("/**/*.md")
    @Secured("ROLE_ADMIN")
    public void add(HttpServletRequest request){
        String blogId = getBlogId(request);
        blogDocumentService.add(blogId);
    }

    @GetMapping("/**/*.md")
    @Secured("ROLE_ADMIN")
    public BlogDocument get(HttpServletRequest request){
        String blogId = getBlogId(request);
        return blogDocumentService.get(blogId);
    }

    private String getBlogId(HttpServletRequest request){
        String path = request.getRequestURI();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {

        }
        String blogId = path.substring(1,path.lastIndexOf(".md"));
        return blogId;
    }
}
