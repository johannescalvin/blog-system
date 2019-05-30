package org.atlassian.commonmark.ext.comment;

import org.atlassian.commonmark.ext.comment.internal.CommentBlock;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.Node;

public interface MetadataRenderer {

    default String render(Node document) {
        StringBuilder builder = new StringBuilder();
        if (document == null){
            return builder.toString();
        }

        Node node = document.getFirstChild();
        while (node != null){
            if (node instanceof CustomBlock){
                CommentBlock block = (CommentBlock)node;
                String html = render(block);
                builder.append(html);
            }
            node = node.getNext();
        }
        return builder.toString();
    }

    default String render(CommentBlock commentBlock){
        StringBuilder builder = new StringBuilder();
        if (commentBlock == null){
            return builder.toString();
        }

        Node child = commentBlock.getFirstChild();
        while (child != null && child instanceof CommentNode){
            CommentNode commentNode = (CommentNode)child;
            String html = render(commentNode);
            builder.append(html);
            child = child.getNext();
        }

        return builder.toString();
    }

    String render(CommentNode commentNode);
}
