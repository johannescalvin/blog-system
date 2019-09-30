package tech.freecode.commonmark.ext.visitors;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;
import tech.freecode.commonmark.ext.comment.CommentNode;

import java.util.LinkedHashSet;

public class AuthorVisitor extends AbstractVisitor {
    private LinkedHashSet<String> authorSet = new LinkedHashSet<>();
    @Override
    public void visit(CustomNode customNode) {
        if (!(customNode instanceof CommentNode)){
            return;
        }

        CommentNode commentNode = (CommentNode)customNode;
        if ("author".equals(commentNode.getType())){
            String text = commentNode.getText();
            if (text == null || text.trim().length() == 0){
                return;
            }

            String[] authors = text.split(",|;|;");
            for (String author : authors){
                author = author.trim();
                if (author.length() != 0){
                    authorSet.add(author);
                }
            }
        }

    }

    public LinkedHashSet<String> getAuthors() {
        return authorSet;
    }
}
