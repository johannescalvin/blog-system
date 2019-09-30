package tech.freecode.blogsystem;

import org.commonmark.node.Document;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.junit.Test;
import tech.freecode.blogsystem.utils.FileUtils;
//import tech.freecode.commonmark.ext.visitors.FullTextVisitor;

public class FullTextVisitorTest {
    String markdown = FileUtils.getFileContentFromDisk("/home/calvin/data/markdown-blog/blog/blogsys/accessability_detection_of_url_in_markdown.md");

    @Test
    public void test(){
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);

        TextContentRenderer renderer = TextContentRenderer.builder().build();

        String s = renderer.render(document);
        System.out.println(s);
    }

}
