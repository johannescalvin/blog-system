package tech.freecode.commonmark.ext.url.accessibility;

import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.NodeRenderer;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class UrlAccessibilityDectector implements NodeRenderer {
    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return new HashSet<>(Arrays.asList(
               Link.class,
                Image.class
        ));
    }

    @Override
    public void render(Node node) {

        if (node instanceof Link){
            Link link = (Link)node;


            Url url = Url.fromLink(link);
            detect(url);

//            System.out.println(url);

        }else if (node instanceof Image){
            Image image = (Image)node;
            Url url = Url.fromImage(image);
            detect(url);
            //            System.out.println(url);
        }
    }


    // 基于文件系统的检测 和 基于 URL系统的检测不一样
    // 为了图方便,现在先只实现基于Linux文件系统的检测
    private void detect(Url url){

        if (isEmailLink(url)){
            // 如果是邮件地址,验证方式比较麻烦,总不能真的发邮件吧
            // 弃坑 弃坑
            return;
        }

        if (isExternalUrl(url)){
            return;
        }

        // 不鼓励在markdown中使用基于本地文件系统的绝对路径
        // 直接禁止,报错
        if(isAbsolutePath(url.getDestination())){
            //TODO 报告问题
            return;
        }

        if (isRelativePath(url.getDestination())){
            String filePath = getAbsolutePath(url.getDestination());
            if (filePath == null){
                System.out.println(url.getDestination() + " 不能转化为一个有效路径");
                return;
            }
            File file = new File(filePath);
            if (!file.exists()){
                //TODO 文件不存在
                System.out.println("文件 " + filePath +" 不存在");
            }else if (!file.canRead()){
                //TODO 文件不可读
                System.out.println("文件 " + filePath +" 不可读");
            }
        }


    }

//    https——用安全套接字层传送的超文本传输协议
//    ftp——文件传输协议
//    mailto——电子邮件地址
//    ldap——轻型目录访问协议搜索
//    file——当地电脑或网上分享的文件
//    news——Usenet新闻组
//    gopher——Gopher协议
//    telnet——Telnet协议
    private boolean isExternalUrl(Url url){
        String[] protocols = new String[]{"http://","https://","file://","www."};
        String dest = url.destination.toLowerCase().trim();
        for (String protocol : protocols){
            if (dest.startsWith(protocol)){
                return true;
            }
        }

        //TODO 规则还没有定完善
        // 比如 baidu.com 这种

        return false;

    }



    private boolean isRelativePath(String filename){
        String relative_path_regex = "(\\.\\.\\/)*"  // 表示父目录的可以出现或者多次连续出现0次
                + "(\\.\\/)?"         // ./ 表示当前目录的,可以至多可以一次
                +  "(\\w[\\w | \\_ | \\- | \\.]*\\/)*"        // 路径 以字母开头,之后可以包含特殊字符 _ - .
                +  "\\w[\\w | \\_ | \\- | \\.]*" ;          // filename;

        Pattern relative_path_pattern = Pattern.compile(relative_path_regex);

        return relative_path_pattern.matcher(filename).matches();
    }

    // 绝对路径,不允许出现
    private boolean isAbsolutePath(String filename){
        if (filename.startsWith("/") || filename.startsWith("~/")){
            return true;
        }
        return false;
    }
    private boolean isEmailLink(Url url){
        if (url.getDestination() == null){
            return false;
        }
        return url.getDestination().trim().toLowerCase().startsWith("mailto:");
    }

    private String fileBase = "/home/calvin/documents/markdown-blog/yangchengzhu/blogsys";
    private String getAbsolutePath(String relativePath){
        String path = fileBase;
        while(path.endsWith("/")){
            path = path.substring(0,path.length()-1);
        }

        while (relativePath.startsWith("../")){
            int index = path.lastIndexOf("/");
            if (index == -1 || index == 0){
                return null;
            }
            path = path.substring(0,index);
            relativePath = relativePath.substring(3);
        }

        if (relativePath.startsWith("./")){
            relativePath.substring(2);
        }

        path = path + "/" + relativePath;

        return path;
    }

    private static class Url {
        private Url(){

        }

        private String achor;
        private String destination;

        @Override
        public String toString() {
            return "Url{" +
                    "achor='" + achor + '\'' +
                    ", destination='" + destination + '\'' +
                    '}';
        }

        static Url fromLink(Link link){
            Url url = new Url();
            url.achor = getAchorText(link);
            url.destination = link.getDestination();
            return url;
        }

        static Url fromImage(Image image){
            Url url = new Url();
            url.achor = getAchorText(image);
            url.destination = image.getDestination();
            return url;
        }

        public String getAchor() {
            return achor;
        }

        public String getDestination() {
            return destination;
        }

        private static String getAchorText(Link link){
            if (link == null){
                return null;
            }

            if (link.getTitle() != null){
                return link.getTitle();
            }

            if ( link.getFirstChild() != null
                    && link.getFirstChild() instanceof Text){
                Text text = (Text) link.getFirstChild();
                return text.getLiteral();
            }
            return null;
        }

        private static String getAchorText(Image image) {
            if (image == null) {
                return null;
            }

            if (image.getTitle() != null) {
                return image.getTitle();
            }

            if (image.getFirstChild() != null
                    && image.getFirstChild() instanceof Text) {
                Text text = (Text) image.getFirstChild();
                return text.getLiteral();
            }
            return null;
        }
    }


}
