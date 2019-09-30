package tech.freecode.blogsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class KeywordsController {

    @GetMapping("/keyword_cloud")
    Iterable<Keyword> keywords(){
        List<Keyword> keywords = new ArrayList<>(16);
        keywords.add(new Keyword("Sam S Club",10000));
        keywords.add(new Keyword("Johannes Calvin",1000));
        keywords.add(new Keyword("Java",20000));
        keywords.add(new Keyword("Neo4j",10000));
        return keywords;

    }

    public static class Keyword{
        private String name;
        private int value;

        public Keyword(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
