package tech.freecode.commonmark.ext.comment.internal;

import tech.freecode.commonmark.ext.comment.CommentNode;
import tech.freecode.commonmark.ext.comment.MetadataRenderer;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.HashMap;

public class MetadataRendereImpl implements MetadataRenderer {
    private MetadataValidator metadataValidator = new MetadataValidator();
    public String render(CommentNode commentNode){
        StringBuilder sb = new StringBuilder();
        HtmlWriter htmlWriter = new HtmlWriter(sb);

        if (metadataValidator.isValid(commentNode)){
            return "";
        }

        renderMeta(commentNode,htmlWriter);
        renderTitle(commentNode,htmlWriter);

        return sb.toString();
    }


    private void renderMeta(CommentNode commentNode,HtmlWriter htmlWriter){
        htmlWriter.line();
        HashMap<String,String> attrMap = new HashMap<>();
        attrMap.put("name",commentNode.getType());
        attrMap.put("lang",commentNode.getSymbol());
        attrMap.put("content",commentNode.getText());
        htmlWriter.tag("meta",attrMap,true);
        htmlWriter.line();
    }

    private void renderTitle(CommentNode commentNode,HtmlWriter htmlWriter){
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
