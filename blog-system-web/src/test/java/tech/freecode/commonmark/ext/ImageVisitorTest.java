package tech.freecode.commonmark.ext;

import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.junit.Test;
import tech.freecode.commonmark.ext.url.accessibility.ImageVisitor;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImageVisitorTest {
    @Test
    public void  image(){
        String markdown = "# 连接测试\n" +
                "![本地图片](images/exmaple.png)\n" +
                "[本站链接](blog/test.html)\n" +
                "[完整URL](http://www.baidu.com)\n" +
                "www.baidu.com";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        ImageVisitor visitor = new ImageVisitor();
        document.accept(visitor);
        List<Image> images = visitor.getImages();
        assertEquals(1,images.size());
        assertTrue(images.get(0).getFirstChild() instanceof Text);
        assertEquals("本地图片",((Text)images.get(0).getFirstChild()).getLiteral());
        assertEquals("images/exmaple.png",images.get(0).getDestination());

    }
}
