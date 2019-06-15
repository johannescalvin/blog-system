package tech.freecode.blogsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidator;
import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidatorUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class UrlAccessabilityValidationConfig {


    @Value("${markdown-file-base}")
    private String fileBase;

    @Scheduled(fixedRate = 1000 * 30 )
//    @Profile("develop")
    public void doTask(){
        task();
    }

//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 )
//    @Profile("product")
//    public void doTask2(){
//        task();
//    }

    private void task(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateFormat.format(new Date()));

        UrlAccessibilityValidatorUtils validatorUtils = new UrlAccessibilityValidatorUtils();
        validatorUtils.setFilebase(fileBase);


        List<File> files = listFiles(new File(fileBase));
        List<UrlAccessibilityValidator.ValidationResult> results = new ArrayList<>();
        for (File markdownFile : files){
            results.addAll(validatorUtils.validate(markdownFile));
        }

        for (UrlAccessibilityValidator.ValidationResult result : results){
            if (result.getStatus() != UrlAccessibilityValidator.ValidationResult.Status.OK){
                System.out.println(result.getMsg());
            }
        }
        System.out.println(results.size());
    }

    public List<File> listFiles(File file){
        ArrayList<File> list = new ArrayList<>();
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files){
                list.addAll(listFiles(f));
            }
        }else if (file.getName().endsWith(".md")){
            list.add(file);
        }

        return list;
    }




}
