package tech.freecode.blogsystem.service;

import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.controller.KeywordsController;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordsService {


    public Iterable<KeywordsController.Keyword> stat(){
        List<KeywordsController.Keyword> keywords = new ArrayList<>(16);
        keywords.add(new KeywordsController.Keyword("Sam S Club",10000));
        keywords.add(new KeywordsController.Keyword("Johannes Calvin",1000));
        keywords.add(new KeywordsController.Keyword("Java",20000));
        keywords.add(new KeywordsController.Keyword("Neo4j",10000));
        return keywords;

    }
}
