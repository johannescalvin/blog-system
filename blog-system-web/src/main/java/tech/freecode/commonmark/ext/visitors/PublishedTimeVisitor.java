package tech.freecode.commonmark.ext.visitors;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;
import tech.freecode.commonmark.ext.comment.CommentNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PublishedTimeVisitor extends AbstractVisitor {
    private Date date = null;
    @Override
    public void visit(CustomNode customNode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!(customNode instanceof CommentNode)){
            return;
        }

        CommentNode commentNode = (CommentNode)customNode;
        if ("published_time".equals(commentNode.getType())){
            String text = commentNode.getText();

            try{
                date = dateFormat.parse(text);
            }catch (ParseException ex){

            }

        }

    }

    public Date getCreatedTime() {
        return date;
    }
}
