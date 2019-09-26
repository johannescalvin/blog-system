package tech.freecode.commonmark.ext.visitors;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;
import tech.freecode.commonmark.ext.comment.CommentNode;

public class DescriptionVisitor extends AbstractVisitor {
    private String description  = null;
    @Override
    public void visit(CustomNode customNode) {
        if (!(customNode instanceof CommentNode)){
            return;
        }

        CommentNode commentNode = (CommentNode)customNode;
        if ("description".equals(commentNode.getType())){
            String text = commentNode.getText();
            if (text == null || text.trim().length() == 0){
                description = text;
            }
        }

    }

    public String getDescription() {
        return description;
    }
}
