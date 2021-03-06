package tech.freecode.blogsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.utils.FileUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;

@Service
public class ElasticsearchBlogSystemInitService {
    @Value("${blog-system.markdown-file-base}")
    private String fileBase;

    @Resource
    private BlogDocumentService blogDocumentService;

    @PostConstruct
    public void init(){
        for (File file : FileUtils.listFiles(fileBase,".md")){
            String blogId = file.getAbsolutePath().substring(fileBase.length(),
                    file.getAbsolutePath().toLowerCase().lastIndexOf(".md"));
            blogDocumentService.add(blogId);
        }
    }
}
