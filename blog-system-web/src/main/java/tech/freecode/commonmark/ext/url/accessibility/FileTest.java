package tech.freecode.commonmark.ext.url.accessibility;

import tech.freecode.commonmark.ext.comment.CommentExtension;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import tech.freecode.blogsystem.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class FileTest {
    public static void main(String[] args) {
        String filename = "/home/calvin/documents/markdown-blog/yangchengzhu/blogsys/generate_catalog_of_markdown.md";
        String markdown = FileUtils.getFileContentFromDisk(filename);
        List<Extension> extensions = new ArrayList<Extension>();
        extensions.add(StrikethroughExtension.create());
        extensions.add(TablesExtension.create());
        extensions.add(AutolinkExtension.create());
        extensions.add(InsExtension.create());
        extensions.add(CommentExtension.create());
        extensions.add(HeadingAnchorExtension.create());
        extensions.add(LinkAccessabilityExtension.create());



        Parser parser = Parser.builder().extensions(extensions).build();

        Node document = parser.parse(markdown);

        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

        String html = renderer.render(document);

        System.out.println(html);
    }
}
