package tech.freecode.blogsystem.cache;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.concurrent.TimeUnit;

@Component
public  class CachingSetup implements JCacheManagerCustomizer {

    @Override
    public void customize(CacheManager cacheManager)
    {
      cacheManager.createCache("markdownFile", new MutableConfiguration<>()
        .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 60*60*24)))
        .setStoreByValue(false)
        .setStatisticsEnabled(true));
    }
}