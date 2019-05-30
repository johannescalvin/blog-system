package tech.freecode.blogsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import tech.freecode.blogsystem.domain.Blog;
import tech.freecode.blogsystem.service.Caches;

import java.util.LinkedList;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model){
        List<Blog> blogList = new LinkedList<>();
        for (Blog blog : Caches.blogs){
            blogList.add(blog);
        }
        model.addAttribute("blogs", blogList);
        return "index";
    }
}
