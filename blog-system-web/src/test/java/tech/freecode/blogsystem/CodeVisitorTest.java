package tech.freecode.blogsystem;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.Test;
import tech.freecode.commonmark.ext.visitors.CodeVisitor;

public class CodeVisitorTest {
    @Test
    public void test(){
        String markdown = "# Title\n" +
                "```shell\n" +
                "tree $pwd\n" +
                "```";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);

        CodeVisitor visitor = new CodeVisitor();
        document.accept(visitor);

        System.out.println(visitor.getCodes());
    }
}
