package tech.freecode.commonmark.ext.url.accessibility;

import tech.freecode.commonmark.ext.comment.CommentExtension;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.ArrayList;
import java.util.List;

public class AccessibilityDetection {
    public static void main(String[] args) {
        List<Extension> extensions = new ArrayList<Extension>();
        extensions.add(StrikethroughExtension.create());
        extensions.add(TablesExtension.create());
        extensions.add(AutolinkExtension.create());
        extensions.add(InsExtension.create());
        extensions.add(CommentExtension.create());
//        extensions.add(LinkAccessabilityExtension.create());

        Parser parser = Parser.builder().extensions(extensions).build();

        String external_url = "[text](http://baidu.com)\n";
        String internal_relative_url = "[text](hello/greeting.html)\n";

        String markdown = "baidu.com\n"
                + "http://baidu.com\n"
                + "yangchengzhu2011@163.com\n"
                + external_url + internal_relative_url;


        String[] strs = new String[]{
                "../filename",
                "../../filename",
                "../../../filename",
                "../../.././filename",
                "../../../path-0/0123-sep_otherSep.suffix",
                "../../../path-0/file_name",
                "../../../path0/file-name",
                "../../../path0/path1/filename.suffix",
                "./filename",
                "./path/test.html",
                "./path/path1/test.html"
        };



        for (String str : strs){
            markdown += "\n["+str+"](" + str + ")";
        }


        Node document = parser.parse(markdown);

        System.out.println(markdown);
//        detect(document);

        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();


        String html = renderer.render(document);

        System.out.println(html);

    }




}

