package tech.freecode.commonmark.ext.languages;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;

import java.util.LinkedHashSet;
import java.util.Set;
// 获取文档中涉及的代码语言种类
public class LanguageVisitor extends AbstractVisitor {

    private Set<String> languages = new LinkedHashSet<>();
    public Set<String> getLanguages(){
        return languages;
    }
    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        super.visit(fencedCodeBlock);
        String language = fencedCodeBlock.getInfo();
        languages.add(language);
    }
}
