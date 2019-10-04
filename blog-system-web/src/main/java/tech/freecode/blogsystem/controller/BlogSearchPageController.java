package tech.freecode.blogsystem.controller;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.freecode.blogsystem.config.IndexPageProps;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.blogsystem.repository.BlogDocumentRepository;
import tech.freecode.blogsystem.service.KeywordsService;

import javax.annotation.Resource;

@Controller
public class BlogSearchPageController {
    @Resource
    private IndexPageProps indexPageProps;
    @Resource
    private KeywordsService keywordsService;

    @Resource
    private BlogDocumentRepository blogDocumentRepository;

    @GetMapping("/search")
    public String page(Model model,
                         @RequestParam(name = "query",required = true) String query){
        model.addAttribute("indexPageProps",indexPageProps);
        model.addAttribute("keywordsStat",keywordsService.keywords(null));
        return "search";
    }

    @GetMapping("/search0")
    @ResponseBody
    public Page<BlogDocument> search(@RequestParam(name = "query",required = true) String queryText){
        QueryBuilder query = QueryBuilders.boolQuery()
                .should(
                        QueryBuilders.queryStringQuery(queryText)
                                .lenient(true)
                                .field("title")
                );
        return blogDocumentRepository.search(query, PageRequest.of(0,10));
    }

    @GetMapping("/advancedSearch")
    public Page<BlogDocument> advancedSearch(){
        return blogDocumentRepository.search(null,null);
    }
}
