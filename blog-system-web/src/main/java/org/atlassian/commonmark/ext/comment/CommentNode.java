package org.atlassian.commonmark.ext.comment;

import org.commonmark.node.CustomNode;

import java.util.regex.Pattern;

public class CommentNode extends CustomNode {
    private String type;
    private String symbol;
    private String text;

    @Override
    public String toString() {
        return "CommentNode{" +
                "type='" + type + '\'' +
                ", symbol='" + symbol + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public CommentNode(String type, String symbol, String text) {
        this.type = type;
        this.symbol = symbol;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getText() {
        return text;
    }
}
