//package tech.freecode.blogsystem.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import tech.freecode.blogsystem.service.LinkAccessibilityService;
//import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidator;
//import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidatorUtils;
//
//import javax.annotation.Resource;
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Configuration
//@EnableScheduling
//public class UrlAccessabilityValidationConfig {
//
//
//    @Value("${markdown-file-base}")
//    private String fileBase;
//
//    @Resource
//    private LinkAccessibilityService linkAccessibilityService;
//
//    @Scheduled(fixedRate = 1000 * 60  )
//    @Profile("develop")
//    public void doTask(){
//        task();
//    }
//
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 )
//    @Profile("production")
//    public void doTask2(){
//        task();
//    }
//
//    private void task(){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        System.out.println(dateFormat.format(new Date()) +" start link accessibility validation in markdown files ");
//        List<File> files = listFiles(new File(fileBase));
//        List<UrlAccessibilityValidator.ValidationResult> results = new ArrayList<>();
//        for (File markdownFile : files){
//            List<UrlAccessibilityValidator.ValidationResult> validationResults = UrlAccessibilityValidatorUtils.validate(markdownFile);
//            for (UrlAccessibilityValidator.ValidationResult result : validationResults){
//                if (result.getStatus() == UrlAccessibilityValidator.ValidationResult.Status.FAIL){
//                    results.add(result);
//                }
//
//            }
//
//            List<UrlAccessibilityValidator.ValidationResult> failedValidationResults =
//                    validationResults
//                            .stream()
//                            .filter((term)-> term.getStatus() ==  UrlAccessibilityValidator.ValidationResult.Status.FAIL)
//                            .collect(Collectors.toList());
//
//
//            if (failedValidationResults.size() == 0){
//                continue;
//            }
//
//            LinkAccessibilityService.AccessibilityFailReport report =
//                    new LinkAccessibilityService.AccessibilityFailReport(
//                            dateFormat.format(new Date()),
//                            markdownFile.getAbsolutePath().substring(fileBase.length()+1)
//                            );
//        }
//
//        for (UrlAccessibilityValidator.ValidationResult result : results){
//            if (result.getStatus() != UrlAccessibilityValidator.ValidationResult.Status.OK){
//                System.out.println(result.getMsg());
//            }
//        }
//
//        System.out.println(dateFormat.format(new Date()) +" end link accessibility validation in markdown files ");
//    }
//
//    public List<File> listFiles(File file){
//        ArrayList<File> list = new ArrayList<>();
//        if (file.isDirectory()){
//            File[] files = file.listFiles();
//            for (File f : files){
//                list.addAll(listFiles(f));
//            }
//        }else if (file.getName().endsWith(".md")){
//            list.add(file);
//        }
//
//        return list;
//    }
//
//
//
//
//}
package tech.freecode.blogsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tech.freecode.blogsystem.service.LinkAccessibilityService;
import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidator;
import tech.freecode.commonmark.ext.url.accessibility.UrlAccessibilityValidatorUtils;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class UrlAccessabilityValidationConfig {


    @Value("${markdown-file-base}")
    private String fileBase;

    @Resource
    private LinkAccessibilityService linkAccessibilityService;

    @Scheduled(fixedRate = 1000 * 60  )
    @Profile("develop")
    public void doTask(){
        task();
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 )
    @Profile("production")
    public void doTask2(){
        task();
    }

    private void task(){
        linkAccessibilityService.clear();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateFormat.format(new Date()) +" start link accessibility validation in markdown files ");

        List<File> files = listFiles(new File(fileBase));
        List<UrlAccessibilityValidator.ValidationResult> results = new ArrayList<>();
        for (File markdownFile : files){
            List<UrlAccessibilityValidator.ValidationResult> validationResults = UrlAccessibilityValidatorUtils.validate(markdownFile);
            for (UrlAccessibilityValidator.ValidationResult result : validationResults){
                if (result.getStatus() == UrlAccessibilityValidator.ValidationResult.Status.FAIL){
                    results.add(result);
                }

            }

            List<String> failedMsgs = validationResults
                    .stream()
                    .filter((term)-> term.getStatus() ==  UrlAccessibilityValidator.ValidationResult.Status.FAIL)
                    .map(term -> term.getMsg())
                    .collect(Collectors.toList());



            if (failedMsgs.size() == 0){
                continue;
            }

            LinkAccessibilityService.AccessibilityFailReport report =
                    new LinkAccessibilityService.AccessibilityFailReport(
                            dateFormat.format(new Date()),
                            markdownFile.getAbsolutePath().substring(fileBase.length()),
                            failedMsgs
                            );
            linkAccessibilityService.addReport(report);
        }



        System.out.println(dateFormat.format(new Date()) +" end link accessibility validation in markdown files ");
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
