package tech.freecode.blogsystem;

import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.Test;
import tech.freecode.commonmark.ext.comment.CommentExtension;
import tech.freecode.commonmark.ext.visitors.AuthorVisitor;

import java.util.ArrayList;
import java.util.List;

public class AuthorVisitorTest {


    @Test
    public void test(){
        String markdown = "[author]: zh (杨承铸,yangchengzhu2011@163.com)\n" +
                "[author]: en (freecode.tech,yangchengzhu)\n" +
                "[title]: en (accessability_detection_of_url_in_markdown)\n" +
                "[title]: zh (markdown文件中的链接可达性检测)\n" +
                "[keywords]: en (accessability detection,markdown)\n" +
                "[keywords]: zh (可达性检测,链接可达性，URL可达性)\n" +
                "[description]: en () \n" +
                "[description]: zh () \n" +
                "[catagory]: en ()\n" +
                "[catagory]: zh ()\n" +
                "[published_time]: Beijing (2018-10-10 23:52:14)\n" +
                "\n" +
                "# markdown文件中的链接可达性检测 \n" +
                "\n" +
                "markdown写起来确实爽，简单、高效。但为了保证markdown出现的链接的可访问性，却要下一番功夫。不仅外部链接可能因为各种原因失效，内部链接也可能因为文件迁移而失效而失效。但偏偏强迫症们又希望自己写的markdown生存期能够长一点。那么在各个节点进行链接有效性的检测就很有必要了。\n";
        List<Extension> extensions = new ArrayList<>();
        extensions.add(CommentExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(markdown);

        AuthorVisitor visitor = new AuthorVisitor();
        document.accept(visitor);

    }
}
