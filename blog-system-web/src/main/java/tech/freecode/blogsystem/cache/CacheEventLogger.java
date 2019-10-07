package tech.freecode.blogsystem.cache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<Object, Object> {

  private final Logger LOG = LoggerFactory.getLogger(CacheEventLogger.class);

  @Override
  public void onEvent(CacheEvent<?, ?> cacheEvent) {
    LOG.info("Key: {} | EventType: {} ",
             cacheEvent.getKey(), cacheEvent.getType());
  }

}