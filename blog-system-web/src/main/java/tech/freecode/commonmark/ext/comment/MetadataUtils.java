package tech.freecode.commonmark.ext.comment;

import tech.freecode.commonmark.ext.comment.internal.MetadataValidator;
import org.commonmark.node.Node;

import java.util.ArrayList;
import java.util.List;

public class MetadataUtils {
    private CommentNodeUtils commentNodeUtils = new CommentNodeUtils();
    public MetadataValidator metadataValidator = new MetadataValidator();

    public List<CommentNode>  getCommentNodes(Node document){
        List<CommentNode> metadatas = new ArrayList<>();

        List<CommentNode> commentNodes = commentNodeUtils.getCommentNodes(document);
        for (CommentNode commentNode :commentNodes){
            if (metadataValidator.isValid(commentNode)){
                metadatas.add(commentNode);
            }
        }
        return metadatas;
    }
}
