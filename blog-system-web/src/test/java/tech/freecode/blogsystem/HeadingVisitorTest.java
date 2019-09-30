package tech.freecode.blogsystem;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import tech.freecode.commonmark.ext.visitors.HeadingVisitor;


public class HeadingVisitorTest {

    private Parser parser;
    private Node document;

    @Before
    public void init(){
        parser = Parser.builder().build();

        String markdown = "# Title\n" +
                "content\n" +
                "## subTitle\n" +
                "content detail\n";

        document = parser.parse(markdown);
    }

    @Test
    public void test(){
        HeadingVisitor visitor = new HeadingVisitor();
        document.accept(visitor);

        System.out.println(visitor.getTitles());


    }

}
