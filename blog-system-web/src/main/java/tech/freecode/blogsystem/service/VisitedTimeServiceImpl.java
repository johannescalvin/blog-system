package tech.freecode.blogsystem.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VisitedTimeServiceImpl implements VisitedTimeService {
    @Override
    public long getVisitedTimes(String id) {
        return new Random().nextLong();
    }
}
