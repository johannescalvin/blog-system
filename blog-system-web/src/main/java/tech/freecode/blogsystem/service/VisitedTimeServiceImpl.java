package tech.freecode.blogsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.domain.BlogDocument;
import tech.freecode.blogsystem.repository.BlogDocumentRepository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VisitedTimeServiceImpl implements VisitedTimeService {

    @Resource
    private BlogDocumentRepository blogDocumentRepository;

    @Value("${blog-system.page-view-times.sync-duration-seconds-threshold}")
    private long threshold;

    private ConcurrentHashMap<String,Long> visitedTimesCache;
    private ConcurrentHashMap<String,Long> timestampCache;

    ExecutorService executorService;

    @PostConstruct
    private void init(){
        int count = (int)blogDocumentRepository.count();
        visitedTimesCache = new ConcurrentHashMap<>(count);
        timestampCache = new ConcurrentHashMap<>(count);

        int pageSize = 20;

        for (int page = 0; page < (count/pageSize + 1); page++){
            Pageable pageable = PageRequest.of(page,pageSize);
            Page<BlogDocument> documents = blogDocumentRepository.findAll(pageable);
            for (BlogDocument document : documents.getContent()){
                visitedTimesCache.put(document.getId(),document.getVisitedTimes());
                timestampCache.put(document.getId(),System.currentTimeMillis());
            }

        }

        executorService = Executors.newSingleThreadExecutor();

    }


    @Override
    public synchronized long getVisitedTimes(String id) {
        return visitedTimesCache.getOrDefault(id,0L);
    }

    public synchronized long incrementAndGet(String id){
        long times = visitedTimesCache.getOrDefault(id,0L);
        times++;
        final long savedTimes = times;
        visitedTimesCache.put(id,times);

        // 距离上一次同步不到指定间隔; 直接返回，不必考虑同步至elasticsearch
        long stamp = timestampCache.getOrDefault(id,System.currentTimeMillis());
        long duration = System.currentTimeMillis() - stamp;
        if ( duration/1000 < threshold){
            return times;
        }

        // 达到间隔时，使用线程池进行异步更新
        // 不阻塞服务，以达到更好的响应时间
        executorService.execute(() -> {
            BlogDocument document = blogDocumentRepository.findById(id).get();
            if (document != null){
                document.setVisitedTimes(savedTimes);
                blogDocumentRepository.save(document);
            }

        });

        return times;

    }


}
