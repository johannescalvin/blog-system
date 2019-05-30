package org.atlassian.commonmark.ext.comment.internal;

import org.atlassian.commonmark.ext.comment.CommentNode;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
@Deprecated
class CommentNodeRenderer  implements NodeRenderer {
    private final HtmlWriter htmlWriter;
    private final HtmlNodeRendererContext context;
    private MetadataValidator metadataValidator = new MetadataValidator();

    public CommentNodeRenderer(HtmlNodeRendererContext context) {
        htmlWriter = context.getWriter();
        this.context = context;
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return
                new HashSet<>(Arrays.asList(
                    CommentNode.class, CommentBlock.class
                ));
    }

    @Override
    public void render(Node node) {

        Node comment = node.getFirstChild();
        if (comment instanceof CommentNode){
            CommentNode commentNode = (CommentNode)comment;
            if (metadataValidator.isValid(commentNode)){
                renderMeta(commentNode);
                renderTitle(commentNode);
            }


        }


    }

    private void renderMeta(CommentNode commentNode){
        htmlWriter.line();
        HashMap<String,String> attrMap = new HashMap<>();
        attrMap.put("name",commentNode.getType());
        attrMap.put("lang",commentNode.getSymbol());
        attrMap.put("content",commentNode.getText());
        htmlWriter.tag("meta",attrMap,true);
        htmlWriter.line();
    }

    private void renderTitle(CommentNode commentNode){
        if ("title".equals(commentNode.getType())){
            htmlWriter.line();
            HashMap<String,String> attrMap = new HashMap<>();
            attrMap.put("lang",commentNode.getSymbol());
            htmlWriter.tag("title",attrMap);
            htmlWriter.text(commentNode.getText());
            htmlWriter.tag("/title");
            htmlWriter.line();
        }

    }
}
