package tech.freecode.commonmark.ext.visitors;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CodeVisitor extends AbstractVisitor {

    private LinkedHashMap<String, List<String>> codes = new LinkedHashMap<>();
    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        String info = fencedCodeBlock.getInfo();
        String literal = fencedCodeBlock.getLiteral();

        List list = codes.getOrDefault(info,new ArrayList<>());
        list.add(literal);

        codes.put(info,list);
    }

    public LinkedHashMap<String, List<String>> getCodes() {
        return codes;
    }
}
