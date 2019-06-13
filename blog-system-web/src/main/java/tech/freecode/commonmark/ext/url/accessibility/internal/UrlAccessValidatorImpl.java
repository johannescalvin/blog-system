package tech.freecode.commonmark.ext.url.accessibility.internal;

import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidator;
import tech.freecode.blogsystem.utils.FileUtils;

import java.io.File;

public class UrlAccessValidatorImpl implements UrlAccessibilityValidator {
    @Override
    public boolean validate(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.canRead()){
            // 如果文件不存在 或者不可读
            return false;
        }
        if (!filename.endsWith(".md")){
            // 只关注markdown文件
            return false;
        }

        String markdown = FileUtils.getFileContentFromDisk(file);
        if (markdown == null || markdown.trim().length() == 0){
            return false;
        }


        return false;
    }
}
