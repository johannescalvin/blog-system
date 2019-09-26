package tech.freecode.blogsystem.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import tech.freecode.commonmark.ext.comment.CommentExtension;
import tech.freecode.commonmark.ext.comment.CommentNode;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.domain.Blog;
import tech.freecode.blogsystem.utils.FileUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
@ConditionalOnClass(tech.freecode.blogsystem.service.ElasticsearchBlogSystemInitService.class)
public class BlogInitService {
    @Value("${blog-system.markdown-file-base}")
    private String fileBase ;

    @Resource
    private Parser parser;

    @PostConstruct
    public void init(){
        init(parser,Caches.blogs);

    }

    public void init(Parser parser,Set<Blog> blogs){
        List<File> files = FileUtils.listFiles(fileBase,".md");
        for (File file : files){
            init(file,parser,blogs);
        }


    }

    private void init(File file,Parser parser,Set<Blog> blogs){
        if (!(file.exists() && file.canRead())){
            return;
        }
        String markdown = FileUtils.getFileContentFromDisk(file);
        Date modifiedDate = new Date(file.lastModified());
        String url = getUrl(file);
        String titles_en = null;
        String titles_zh = null;

        Set<String> keywords = new HashSet<>();
        Set<String> descriptions = new HashSet<>();

        Date publishedDate = null;


        Node document = parser.parse(markdown);

        Node node = document.getFirstChild();
        while (node != null){
            if (node instanceof CustomBlock){
                Node child = node.getFirstChild();
                while (child != null && child instanceof CommentNode){

                    CommentNode commentNode = (CommentNode)child;
                    titles_en = getTitleEn(commentNode,titles_en);
                    titles_zh = getTitleZh(commentNode,titles_zh);
                    getKeyWords(commentNode,keywords);
                    getDescriptions(commentNode,descriptions);
                    getPublishedDate(commentNode,publishedDate);
                    child = child.getNext();
                }
            }
            node = node.getNext();

        }

        Blog blog = new Blog();
        blog.setTitle(titles_zh,titles_en);
        blog.setUrl(url);
        blog.setPublishedDate(publishedDate);
        blog.setModifiedDate(modifiedDate);
        blog.setKeywords(keywords);
        blog.setDescriptions(descriptions);

        if (blog.getTitle() != null && blog.getTitle().trim().length() != 0
                && blog.getUrl() != null && blog.getUrl().length() != 0
                && blog.getModifiedDate() != null){
            blogs.add(blog);
        }

    }

    private void getKeyWords(CommentNode commentNode,Set<String> keywords){
        if ("keywords".equals(commentNode.getType())){
            String text = commentNode.getText();
            String[] array = text.split(";|,|，|、");
            for (String keyword : array){
                keywords.add(keyword);
            }
        }
    }

    private void getDescriptions(CommentNode commentNode,Set<String> descriptions){
        if ("description".equals(commentNode.getType())){
            descriptions.add(commentNode.getText());
        }
    }

    // 以最后一个为准
    private void getPublishedDate(CommentNode commentNode,Date publishedDate){
        if (!"published_time".equals(commentNode.getType())){
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Pattern pattern = Pattern.compile("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d");
        if(pattern.matcher(commentNode.getText()).matches()){
            try{
                publishedDate = sdf.parse(commentNode.getText());
            }catch (ParseException ex){

            }

        }

    }
    // 只承认第一次出现的合格title
    private String getTitleEn(CommentNode commentNode,String title_en){
        if (title_en != null && title_en.trim().length() != 0){
            return title_en;
        }
        if ("title".equals(commentNode.getType())
                && commentNode.getSymbol().startsWith("en")){
            title_en = commentNode.getText();
        }

        return title_en;
    }

    private String getTitleZh(CommentNode commentNode,String title_zh){
        if (title_zh != null && title_zh.trim().length() != 0){
            return title_zh;
        }

        if ("title".equals(commentNode.getType()) &&
                commentNode.getSymbol().equals("zh")){
            title_zh = commentNode.getText();
        }

        return title_zh;
    }


    private String getUrl(File file) {
        String path = file.getAbsolutePath();
        String url = path.substring(fileBase.length(),path.lastIndexOf(".md")) + ".html";
        return url;
    }

    public synchronized void updateBlog(String markdownFilename){
        if (markdownFilename == null
                || markdownFilename.trim().length() == 0
                || !markdownFilename.trim().endsWith(".md")){
            return;
        }

        if (!fileBase.endsWith("/")){
            fileBase += "/";
        }

        String filePath = fileBase  + markdownFilename;
        File file = new File(filePath);
        if (!file.exists()){
            tryRomoveBlogFromCache(Caches.blogs,markdownFilename);
            return;
        }

        List<Extension> extensions = new ArrayList<Extension>();
        extensions.add(StrikethroughExtension.create());
        extensions.add(TablesExtension.create());
        extensions.add(AutolinkExtension.create());
        extensions.add(InsExtension.create());
        extensions.add(CommentExtension.create());

        Parser parser = Parser.builder().extensions(extensions).build();

        init(file,parser,Caches.blogs);

    }

    // 很可能被攻击
    // 必须严格控制调用者权限
    private void tryRomoveBlogFromCache(Set<Blog> blogs,String markdownFilename){
        System.out.println("之前： " + blogs.size());

        String url = markdownFilename.substring(0,markdownFilename.lastIndexOf(".md")) + ".html";
        Iterator<Blog> blogsIter = blogs.iterator();
        while (blogsIter.hasNext()){
            Blog blog = blogsIter.next();
            if (blog.getUrl() != null && blog.getUrl().equals(url)){
                blogsIter.remove();
            }
        }

        System.out.println("之后： " + blogs.size());
    }

    public static void main(String[] args) {
        List<Extension> extensions = new ArrayList<Extension>();
        extensions.add(StrikethroughExtension.create());
        extensions.add(TablesExtension.create());
        extensions.add(AutolinkExtension.create());
        extensions.add(InsExtension.create());
        extensions.add(CommentExtension.create());

        Parser parser = Parser.builder().extensions(extensions).build();
        new BlogInitService().init(parser,Caches.blogs);

        Set<Blog> blogs = Caches.blogs;
        System.out.println(Caches.blogs.size());

        System.out.println(Caches.blogs);
    }


}
