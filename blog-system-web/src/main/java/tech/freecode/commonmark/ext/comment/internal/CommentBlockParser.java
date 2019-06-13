package tech.freecode.commonmark.ext.comment.internal;

import tech.freecode.commonmark.ext.comment.CommentNode;
import org.commonmark.node.Block;
import org.commonmark.parser.InlineParser;
import org.commonmark.parser.block.*;

import java.util.regex.Pattern;

public class CommentBlockParser extends AbstractBlockParser {

    private final CommentBlock block = new CommentBlock();
    private final String content;

    public CommentBlockParser(String content) {
        this.content = content;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
        // 为了简便,我们约定单行注释就是一个Block
        // 所以可以到此为止
        return BlockContinue.none();
    }

    @Override
    public void parseInlines(InlineParser inlineParser) {
        String line = content;
        int startIndex = 0;
        int endIndex = line.indexOf("]:");
        // []:之间必须包含字符
        if (line.charAt(startIndex) == '[' && endIndex != -1 & endIndex != 1){
            String comment_type = line.substring(1,endIndex);
            comment_type = comment_type.trim();

            line = line.substring(endIndex + 2).trim();
            endIndex = line.indexOf(" ");
            if(endIndex == -1){
                endIndex = line.indexOf("\t");
            }

            if(endIndex == -1){
                return;
            }

            String comment_lang = line.substring(0,endIndex);

            line = line.substring(endIndex).trim();

            startIndex = line.indexOf("(");
            endIndex = line.lastIndexOf(")");
            String comment_content = line.substring(startIndex+1,endIndex);

            CommentNode commentNode = new CommentNode(comment_type,comment_lang,comment_content);
            block.appendChild(commentNode);

        }
    }



    public static class Factory extends AbstractBlockParserFactory {
        String PATTERN =
                "\\s{0,3}" // 至多可以有三个空格
                        + "\\["          // [出现一次
                        + "\\s*"
                        + "\\S+"            // 非空白必须至少出现一次
                        + "\\s*"
                        + "\\]"             // ]出现一次
                        + ":"               // 紧跟着出现：
                        + "\\s+"            // 至少出现一次空白符
                        + "\\S+"            // 之后必须出现非空白字符 作为注释标识
                        + "\\s+"            // 注释标识之后必须至少跟一个空白符
                        + "\\(.*\\)"        // 注释主体必须位于()之内,其文本可以是除了\n外的任意字符
                        + "\\s*";           // 允许注释主体之后出现空白符
        Pattern pattern = Pattern.compile(PATTERN);

        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            CharSequence line = state.getLine();
            CharSequence paragraph = matchedBlockParser.getParagraphContent();
            if (pattern.matcher(line.toString()).matches()){
                String content = line.toString().trim();
                return  BlockStart.of(new CommentBlockParser(content)).atIndex(line.length());
            }
            return BlockStart.none();
        }
    }
}
