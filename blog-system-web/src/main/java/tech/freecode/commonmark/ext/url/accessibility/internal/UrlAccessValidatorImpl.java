package tech.freecode.commonmark.ext.url.accessibility.internal;

import tech.freecode.commonmark.ext.url.accessibility.*;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlAccessValidatorImpl implements UrlAccessibilityValidator {

    private String filebase;
    private LocalFile localFile;

    public UrlAccessValidatorImpl(String filebase) {
        this.filebase = filebase;
        localFile = new LinuxLocalFile();
    }

    // 基于文件系统的检测 和 基于 URL系统的检测不一样
    // 为了图方便,现在先只实现基于Linux文件系统的检测
    public ValidationResult validate(Url url){

        if (UrlUtils.isEmailLink(url)){
            // 如果是邮件地址,验证方式比较麻烦,总不能真的发邮件吧
            // 弃坑 弃坑
            ValidationResult result =  new ValidationResult();
            result.setStatus(ValidationResult.Status.IGNORE);
            result.setMsg("Email is merely ignored");
        }

        // 不鼓励在markdown中使用基于本地文件系统的绝对路径
        // 直接禁止,报错
        if(UrlUtils.isAbsolutePath(url)){
            ValidationResult result =  new ValidationResult();
            result.setStatus(ValidationResult.Status.FAIL);
            result.setMsg("Abosulte file path is forbidden in markdown");
            return result;
        }



        if (UrlUtils.isRelativePath(url)){
            ValidationResult result =  new ValidationResult();

            String filePath = localFile.getAbsolutePath(filebase,url.getDestination());
            if (filePath == null){
                result.setStatus(ValidationResult.Status.FAIL);
                result.setMsg("cann't convert to a valid file path");
                System.out.println(url.getDestination() + " 不能转化为一个有效路径");
                return result;
            }

            File file = new File(filePath);
            if (!file.exists()){
                // 文件不存在
                if(!UrlUtils.isExternalUrl(url)){
                    result.setStatus(ValidationResult.Status.FAIL);
                    result.setMsg("文件 " + filePath +" 不存在");
                    return result;
                }

            }else if (!file.canRead()){
                result.setStatus(ValidationResult.Status.FAIL);
                result.setMsg("文件 " + filePath +" 不可读");
                return result;
            }
            result.setStatus(ValidationResult.Status.OK);
            result.setMsg("OK");
        }

        if (UrlUtils.isExternalUrl(url)){

            ValidationResult result =  new ValidationResult();
            //TODO access url
            try{
                url = UrlUtils.wellform(url);
                URL link = new URL(url.getDestination());
                HttpURLConnection conn = (HttpURLConnection) link.openConnection();
                int status = conn.getResponseCode();

                if (status == 200){
                    result.setStatus(ValidationResult.Status.OK);
                    result.setMsg("OK");
                    return result;
                }else {
                    result.setStatus(ValidationResult.Status.FAIL);
                    result.setMsg(url.getDestination() + " can not be accessed.");
                    return result;
                }

            }catch (MalformedURLException ex){
                result.setStatus(ValidationResult.Status.FAIL);
                result.setMsg(url.getDestination() + " is a bad-formed url.");
                return result;
            }catch (IOException ex){
                result.setStatus(ValidationResult.Status.FAIL);
                result.setMsg(url.getDestination() + " can not be accessed.");
                return result;
            }

        }

        ValidationResult result =  new ValidationResult();
        result.setStatus(ValidationResult.Status.IGNORE);
        result.setMsg(url.getDestination() + " is merely ignored");
        return result;

    }



}
