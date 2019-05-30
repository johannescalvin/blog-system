package org.atlassian.commonmark.ext.comment;

import org.atlassian.commonmark.ext.comment.internal.CommentBlock;

import org.commonmark.node.Node;

import java.util.ArrayList;
import java.util.List;

public class CommentNodeUtils  {
    public List<CommentNode> getCommentNodes(Node document){
        ArrayList<CommentNode> commentNodes = new ArrayList<>();

        if (document == null){
            return commentNodes;
        }

        Node node = document.getFirstChild();

        while (node != null){
            if (node instanceof CommentBlock){
                CommentBlock block = (CommentBlock)node;
                commentNodes.addAll( parseCommentNode(block) );
            }

            node = node.getNext();
        }

        return commentNodes;
    }

    List<CommentNode> parseCommentNode(CommentBlock commentBlock){
        ArrayList<CommentNode> commentNodes = new ArrayList<>();

        if (commentBlock == null){
            return commentNodes;
        }

        Node child = commentBlock.getFirstChild();
        while (child != null && child instanceof CommentNode){
            CommentNode commentNode = (CommentNode)child;
            commentNodes.add(commentNode);
            child = child.getNext();
        }

        return commentNodes;
    }

}
