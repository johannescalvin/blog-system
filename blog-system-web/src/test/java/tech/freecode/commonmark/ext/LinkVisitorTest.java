package tech.freecode.commonmark.ext;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.Test;
import tech.freecode.commonmark.ext.url.accessibility.ImageVisitor;
import tech.freecode.commonmark.ext.url.accessibility.LinkVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkVisitorTest {
    @Test
    public void link(){
        String markdown = "# 连接测试\n" +
                "![本地图片](images/exmaple.png)\n" +
                "[本站链接](blog/test.html)\n" +
                "[完整URL](http://www.baidu.com)\n" +
                "www.baidu.com";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        LinkVisitor visitor = new LinkVisitor();
        document.accept(visitor);
        List<Link> images = visitor.getLinks();
        assertEquals(2,images.size());
        assertTrue(images.get(0).getFirstChild() instanceof Text);
        assertEquals("本站链接",((Text)images.get(0).getFirstChild()).getLiteral());
        assertEquals("blog/test.html",images.get(0).getDestination());
    }

    @Test
    public void testAutoLink(){
        List<Extension> extensions = new ArrayList<>();
        extensions.add(AutolinkExtension.create());
        Parser parser = new Parser.Builder().extensions(extensions).build();

        String markdown = "# autolink\n\n" +
                "http://www.baidu.com\n";
        Node document = parser.parse(markdown);

        LinkVisitor visitor = new LinkVisitor();
        document.accept(visitor);

        List<Link> links = visitor.getLinks();

        assertEquals(1,links.size());
        assertEquals("http://www.baidu.com",links.get(0).getDestination());
    }

}
