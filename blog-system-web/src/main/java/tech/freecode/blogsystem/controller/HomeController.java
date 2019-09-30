package tech.freecode.blogsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tech.freecode.blogsystem.config.IndexPageProps;
import tech.freecode.blogsystem.service.KeywordsService;

import javax.annotation.Resource;

@Controller
public class HomeController {
    @Resource
    private IndexPageProps indexPageProps;
    @Resource
    private KeywordsService keywordsService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("indexPageProps",indexPageProps);
        model.addAttribute("keywordsStat",keywordsService.stat());
        return "index";
    }
}
