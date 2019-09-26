package tech.freecode.blogsystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.blogsystem.repository.BlogDocumentRepository;
import tech.freecode.blogsystem.service.BlogDocumentService;

import javax.annotation.Resource;

@Controller
public class BlogController {

    @Resource
    private BlogDocumentService blogDocumentService;

    @GetMapping("search")
    public String page(){
        return "page";
    }

    @GetMapping("/blogs")
    @ResponseBody
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
