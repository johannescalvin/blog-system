package tech.freecode.blogsystem.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tech.freecode.blogsystem.config.IndexPageProps;
import tech.freecode.blogsystem.service.KeywordsService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    @Resource
    private IndexPageProps indexPageProps;
    @Resource
    private KeywordsService keywordsService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("indexPageProps",indexPageProps);
        model.addAttribute("keywordsStat",keywordsService.keywords(null));

        int[] array = new int[]{4300, 10000, 28000, 35000, 50000, 19000};
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : array){
            list.add(i);
        }

        String info = list.toString();
        model.addAttribute("list",list);

        List<Indicator> indicators = new ArrayList<>();
        indicators.add(new Indicator("销售",6000));
        indicators.add(new Indicator("管理",16000));
        indicators.add(new Indicator("信息技术",30000));
        indicators.add(new Indicator("客服",38000));
        indicators.add(new Indicator("研发",52000));
        indicators.add(new Indicator("市场",25000));

        model.addAttribute("indicators",indicators);
        return "index";
    }

    @Data
    private static class Indicator{
        private String text;
        private int max;

        public Indicator(String text, int max) {
            this.text = text;
            this.max = max;
        }
    }

}
