package tech.freecode.blogsystem;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import tech.freecode.commonmark.ext.catalog.HeadingIdExtension;
import tech.freecode.commonmark.ext.comment.CommentExtension;
import tech.freecode.commonmark.ext.comment.MetadataUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties
public class BlogSystemApp {
    public static void main(String[] args) {
        SpringApplication.run(BlogSystemApp.class,args);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataUtils metadataUtils(){
        return new MetadataUtils();
    }

    private static List<Extension> extensions = new ArrayList<Extension>();
    static {
        extensions.add(StrikethroughExtension.create());
        extensions.add(TablesExtension.create());
        extensions.add(AutolinkExtension.create());
        extensions.add(InsExtension.create());
        extensions.add(HeadingIdExtension.create());
        extensions.add(CommentExtension.create());
    }


    @Bean
    public Parser parser(){
        Parser parser = Parser.builder().extensions(extensions).build();
        return parser;
    }

    @Bean
    public HtmlRenderer htmlRenderer(){
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        return renderer;
    }

}
