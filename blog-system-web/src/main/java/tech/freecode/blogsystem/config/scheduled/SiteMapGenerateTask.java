package tech.freecode.blogsystem.config.scheduled;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import tech.freecode.blogsystem.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class SiteMapGenerateTask {

    @Value("${seo.site-map.store-dir}")
    private String storeDir;

    @Value("${markdown-file-base}")
    private String markdownFileDir;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 )
    public void task() throws IOException {
        File storeFile = new File(storeDir);
        if (!storeFile.exists()) {
            storeFile.mkdirs();
        }
        W3CDateFormat dateFormat = new W3CDateFormat(W3CDateFormat.Pattern.DAY);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        WebSitemapGenerator wsg = WebSitemapGenerator
                .builder("http://www.blog.freecode.tech", storeFile)
//                .gzip(true)
//                .fileNamePrefix("blog")
                .dateFormat(dateFormat)
                .build();

        List<File> files = FileUtils.listFiles(new File(markdownFileDir),".md");
        for (File markdownFile : files){
            String path = markdownFile.getAbsolutePath().substring(markdownFileDir.length());
            WebSitemapUrl url = new WebSitemapUrl
                    .Options("http://www.blog.freecode.tech/"+path)
                    .lastMod(new Date(markdownFile.lastModified()))
//                    .priority(1.0)
//                    .changeFreq(ChangeFreq.WEEKLY)
                    .build();
            wsg.addUrl(url); // repeat multiple times
        }


        wsg.write();
        wsg.writeSitemapsWithIndex();
    }
}
