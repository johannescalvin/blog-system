package tech.freecode.blogsystem.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class KeywordsServiceImpl implements KeywordsService{
    @Override
    public Iterable<String> keywords(Pageable pageable) {
        return new ArrayList<>();
    }
}
