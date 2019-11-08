package tech.freecode.blogsystem.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.service.BlogDocumentService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "blog-system.markdown-file-watch-service.enabled",havingValue = "true")
public class FileChangeDetectService {

    private static Logger logger = LoggerFactory.getLogger(FileChangeDetectService.class);

    @Value("${blog-system.markdown-file-base}")
    private String fileBase;
    @Resource
    private BlogDocumentService blogDocumentService;


    private Path directory;

    @PostConstruct
    public void init(){
        directory = Paths.get(fileBase);

        Map<WatchKey,Path> watchKeyPathMap = new HashMap<>();
        new Thread(()->{ monitor();}).start();
    }

    private String getBlogId(Path path){
        return path.normalize().toString().substring(fileBase.length(),
                path.normalize().toString().toLowerCase().lastIndexOf(".md"));
    }

    private void monitor(){
        // 由于WatchKey.context()返回的Path只有最后一级相对路径
        // 没有提供查询当前watchKey关联的Path的完整路径的方法
        // 所以，必须自行实现这种关联
        // 如此，才能知道触发事件的文件/目录的完整路径
        Map<WatchKey,Path> watchKeyPathMap = new HashMap<>();
        try(WatchService watchService = FileSystems.getDefault().newWatchService()){
            Files.walkFileTree(directory,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // 注册
                    WatchKey key = dir.register(watchService,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY);
                    // 关联WatchKey和Path
                    watchKeyPathMap.put(key,dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            while (true){
                WatchKey watchKey = null;
                try{
                    watchKey = watchService.take();
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                if (watchKey == null){
                    continue;
                }

                for (WatchEvent<?> watchEvent : watchKey.pollEvents()){
                    WatchEvent.Kind kind = watchEvent.kind();
                    if (kind == StandardWatchEventKinds.OVERFLOW){
                        continue;
                    }

                    final WatchEvent<Path> pathWatchEvent = (WatchEvent<Path>)watchEvent;
                    Path path = pathWatchEvent.context();

                    Path parent = watchKeyPathMap.get(watchKey);

                    // 获得完整路径
                    path = parent.resolve(path);

                    if (Files.isDirectory(path)){
                        // 注册新增的子目录
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE){
                            try{
                                WatchKey key = path.register(watchService,
                                        StandardWatchEventKinds.ENTRY_CREATE,
                                        StandardWatchEventKinds.ENTRY_DELETE,
                                        StandardWatchEventKinds.ENTRY_MODIFY);

                                watchKeyPathMap.put(key,path);
                            }catch (IOException ex){

                            }
                        }
                        // 如果目录被删除，应该注销WatchSerivce的注册
                        // 并删除 WatchKey和Path的关联
                        if (kind == StandardWatchEventKinds.ENTRY_DELETE){
                            watchKey.cancel();
                            watchKeyPathMap.remove(watchKey,path);
                        }
                    }else if (Files.isRegularFile(path) && path.toString().toLowerCase().endsWith(".md")){
                        // 对普通文件的处理
                        if (StandardWatchEventKinds.ENTRY_CREATE == kind){
                            System.out.println(path + " is created!");
                            blogDocumentService.add(getBlogId(path));
                        }else if (StandardWatchEventKinds.ENTRY_MODIFY == kind){
                            System.out.println(path + " is modified!");
                            blogDocumentService.update(getBlogId(path));
                        }else if (StandardWatchEventKinds.ENTRY_DELETE == kind){
                            System.out.println(path + " is deleted!");
                            blogDocumentService.delete(getBlogId(path));
                        }

                    }
                }
                // 如果重置失败，watchKey变为invalid状态
                // 应该删除watchKey与Path的关联
                boolean valid = watchKey.reset();
                if (!valid){
                    watchKeyPathMap.remove(watchKey);
                    break;
                }
            }

        }catch (IOException ex){

        }


    }
}
