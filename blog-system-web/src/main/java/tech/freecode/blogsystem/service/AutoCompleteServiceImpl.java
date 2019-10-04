package tech.freecode.blogsystem.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.utils.FileUtils;
import tech.freecode.common.algo.utils.tree.BigAlphabetTrie;
import tech.freecode.common.algo.utils.tree.Trie;
import tech.freecode.commonmark.ext.visitors.KeywordsVisitor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class AutoCompleteServiceImpl implements AutoCompleteService {

    @Resource
    private Parser parser;

    @Value("${blog-system.markdown-file-base}")
    private String fileBase;

    Trie<Integer> trie = new BigAlphabetTrie<>();

    @PostConstruct
    public void init(){
        for (File file : FileUtils.listFiles(fileBase,".md")){
            String markdown = FileUtils.getFileContentFromDisk(file);
            if (markdown == null || markdown.length() == 0){
                continue;
            }

            Node document = parser.parse(markdown);
            KeywordsVisitor keywordsVisitor = new KeywordsVisitor();
            document.accept(keywordsVisitor);
            for (String keyword : keywordsVisitor.getKeywords()){
                keyword = keyword.toLowerCase();
                Integer count = trie.get(keyword);
                if (count == null){
                    count = 0;
                }
                count++;

                trie.put(keyword,count);
            }
        }
    }

    @Override
    public List<Item> autoComplete(String prefix) {
        List<String> keys = null;
        if (prefix == null || prefix.trim().length() == 0){
            keys =  trie.keys();
        }else {
            keys = trie.keysWithPrefix(prefix);
        }

        ArrayList<Item> items = new ArrayList<>(keys.size());
        int id = 0;
        for (String text : keys){
            Item item = new Item();
            item.setId(id);
            item.setText(text);
//            item.setSelected("false");
            items.add(item);

            id++;
        }

        return items;

    }
}
