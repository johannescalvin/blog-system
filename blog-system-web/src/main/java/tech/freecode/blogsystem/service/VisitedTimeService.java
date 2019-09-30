package tech.freecode.blogsystem.service;

public interface VisitedTimeService {
    long getVisitedTimes(String id);
    long incrementAndGet(String id);
}
