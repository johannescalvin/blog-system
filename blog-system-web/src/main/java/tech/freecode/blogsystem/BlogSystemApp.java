package tech.freecode.blogsystem;

import org.atlassian.commonmark.ext.comment.MetadataUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogSystemApp {
    public static void main(String[] args) {
        SpringApplication.run(BlogSystemApp.class,args);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataUtils metadataUtils(){
        return new MetadataUtils();
    }

}
