package tech.freecode.blogsystem.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.freecode.blogsystem.controller.KeywordsController;


public interface KeywordsService {
    Iterable<String> keywords(Pageable pageable);
}
