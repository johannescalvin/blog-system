package tech.freecode.commonmark.ext.visitors;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;
import tech.freecode.commonmark.ext.comment.CommentNode;

import java.util.LinkedHashSet;

public class KeywordsVisitor extends AbstractVisitor {

    private LinkedHashSet<String> keywordsSet = new LinkedHashSet<>();
    @Override
    public void visit(CustomNode customNode) {
        if (!(customNode instanceof CommentNode)){
            return;
        }

        CommentNode commentNode = (CommentNode)customNode;
        if ("keywords".equals(commentNode.getType())){
            String text = commentNode.getText();
            if (text == null || text.trim().length() == 0){
                return;
            }

            String[] keywords = text.split(",|，|：|:|；|;|、");
            for (String keyword : keywords){
                keyword = keyword.trim();
                if (keyword.length() != 0){
                    keywordsSet.add(keyword);
                }
            }
        }
    }

    public LinkedHashSet<String> getKeywords() {
        return keywordsSet;
    }
}
