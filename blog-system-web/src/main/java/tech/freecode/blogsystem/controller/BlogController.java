package tech.freecode.blogsystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.blogsystem.service.BlogDocumentServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
public class BlogController {

    @Resource
    private BlogDocumentServiceImpl blogDocumentService;

    @GetMapping("/blogs")
    Page<BlogDocument> getBlogs(@PageableDefault(
                page = 0, size = 10,
                direction = Sort.Direction.DESC)
            @SortDefault.SortDefaults(
                    {@SortDefault(sort = "createdTime",direction = Sort.Direction.DESC),
                    @SortDefault(sort = "modifiedTime",direction = Sort.Direction.DESC)})
                               Pageable pageable){
        return blogDocumentService.findAll(pageable);
    }

    @PutMapping("/**/*.md")
    public void update(HttpServletRequest request) {
        String blogId = getBlogId(request);
        blogDocumentService.update(blogId);
    }

    @DeleteMapping("/**/*.md")
    public boolean delete(HttpServletRequest request){
        String blogId = getBlogId(request);
        return blogDocumentService.delete(blogId);
    }

    @PostMapping("/**/*.md")
    public void add(HttpServletRequest request){
        String blogId = getBlogId(request);
        blogDocumentService.add(blogId);
    }

    @GetMapping("/**/*.md")
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
