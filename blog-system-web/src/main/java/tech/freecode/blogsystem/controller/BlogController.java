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
}
