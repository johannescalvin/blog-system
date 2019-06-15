package tech.freecode.commonmark.ext.url.accessibility;

import java.util.regex.Pattern;

public class UrlUtils {
    //    https——用安全套接字层传送的超文本传输协议
//    ftp——文件传输协议
//    mailto——电子邮件地址
//    ldap——轻型目录访问协议搜索
//    file——当地电脑或网上分享的文件
//    news——Usenet新闻组
//    gopher——Gopher协议
//    telnet——Telnet协议
    public static boolean isExternalUrl(Url url){
        String[] protocols = new String[]{"http://","https://","file://","www."};
        String dest = url.getDestination().toLowerCase().trim();
        for (String protocol : protocols){
            if (dest.startsWith(protocol)){
                return true;
            }
        }

        //TODO 规则还没有定完善
        // 比如 baidu.com 这种

        return false;

    }



    public static boolean isRelativePath(Url url){
        String filename = url.getDestination();
        String relative_path_regex = "(\\.\\.\\/)*"  // 表示父目录的可以出现或者多次连续出现0次
                + "(\\.\\/)?"         // ./ 表示当前目录的,可以至多可以一次
                +  "(\\w[\\w | \\_ | \\- | \\.]*\\/)*"        // 路径 以字母开头,之后可以包含特殊字符 _ - .
                +  "\\w[\\w | \\_ | \\- | \\.]*" ;          // filename;

        Pattern relative_path_pattern = Pattern.compile(relative_path_regex);

        return relative_path_pattern.matcher(filename).matches();
    }

    // 绝对路径,不允许出现
    public static boolean isAbsolutePath(Url url){
        String filename = url.getDestination();
        if (filename.startsWith("/") || filename.startsWith("~/")){
            return true;
        }
        return false;
    }
    public static boolean isEmailLink(Url url){
        if (url == null || url.getDestination() == null){
            return false;
        }
        return url.getDestination().trim().toLowerCase().startsWith("mailto:");
    }

    public static Url wellform(Url link){
        String dest = link.getDestination().trim().toLowerCase();
        if (!dest.startsWith("https://") && !dest.startsWith("http://")){
            dest = "http://" + dest;
            return new Url(dest,link.getTitle());
        }

        return link;

    }

}
