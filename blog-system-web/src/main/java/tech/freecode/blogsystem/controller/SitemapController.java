package tech.freecode.blogsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.freecode.blogsystem.utils.FileUtils;

import java.io.File;

@Controller
public class SitemapController {
    @Value("${blog-system.seo.site-map.store-dir}")
    private String siteMapFileStoreDir;

    @GetMapping(value = "/admin/site-map/{filename}",produces = "application/xml;charset=UTF-8")
    @ResponseBody
    public String get(@PathVariable String filename){
        String xml = FileUtils.getFileContentFromDisk(new File(siteMapFileStoreDir+filename));
        return xml;
    }
}
