package tech.freecode.commonmark.ext.url.accessibility;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import tech.freecode.blogsystem.utils.FileUtils;
import tech.freecode.commonmark.ext.url.accessibility.internal.UrlAccessValidatorImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UrlAccessibilityValidatorUtils  {
    private String filebase;

    public void setFilebase(String filebase) {
        this.filebase = filebase;
    }

    public List<UrlAccessibilityValidator.ValidationResult> validate(File file){
        ArrayList<UrlAccessibilityValidator.ValidationResult> validationResults = new ArrayList<>();

        if (!file.exists() || !file.canRead()){
            // 如果文件不存在 或者不可读
            return validationResults;
        }
        if (!file.getName().endsWith(".md")){
            // 只关注markdown文件
            return validationResults;
        }

        String markdown = FileUtils.getFileContentFromDisk(file);
        if (markdown == null || markdown.trim().length() == 0){
            return validationResults;
        }

        return validate(markdown);
    }


    public List<UrlAccessibilityValidator.ValidationResult> validate(String markdown){
        ArrayList<UrlAccessibilityValidator.ValidationResult> validationResults = new ArrayList<>();

        List<Extension> extensions = new ArrayList<>();
        extensions.add(AutolinkExtension.create());

        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(markdown);

        validationResults.addAll(validateImages(document));
        validationResults.addAll(validateLinks(document));

        return validationResults;
    }

    private List<UrlAccessibilityValidator.ValidationResult> validateImages(Node document){
        UrlAccessibilityValidator validator = new UrlAccessValidatorImpl(filebase);

        ImageVisitor imageVisitor = new ImageVisitor();
        document.accept(imageVisitor);
        List<Image> images = imageVisitor.getImages();

        ArrayList<UrlAccessibilityValidator.ValidationResult> validationResults = new ArrayList<>();
        for (Image image : images){
            Url url = Url.of(image);
            UrlAccessibilityValidator.ValidationResult validationResult = validator.validate(url);
            validationResults.add(validationResult);
        }

        return validationResults;

    }

    private List<UrlAccessibilityValidator.ValidationResult> validateLinks(Node document){
        UrlAccessibilityValidator validator = new UrlAccessValidatorImpl(filebase);

        LinkVisitor linkVisitor = new LinkVisitor();
        document.accept(linkVisitor);
        List<Link> links = linkVisitor.getLinks();

        ArrayList<UrlAccessibilityValidator.ValidationResult> validationResults = new ArrayList<>();
        for (Link link : links){
            Url url = Url.of(link);
            UrlAccessibilityValidator.ValidationResult validationResult = validator.validate(url);
            validationResults.add(validationResult);
        }

        return validationResults;

    }
}
