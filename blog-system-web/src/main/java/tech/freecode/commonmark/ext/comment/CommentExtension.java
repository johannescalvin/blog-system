package tech.freecode.commonmark.ext.comment;

import tech.freecode.commonmark.ext.comment.internal.CommentBlockParser;
import org.commonmark.Extension;
import org.commonmark.parser.Parser;

public class CommentExtension implements Parser.ParserExtension {

    public static Extension create(){
        return new CommentExtension();
    }

    // 添加关于Markdown注释的解析规则
    // markdown中，注释都是单行的
    // 在commonmark中,单行也要当作block处理,否则没法将解析到的CustomNode添加到documents树中
    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new CommentBlockParser.Factory());
    }
}
