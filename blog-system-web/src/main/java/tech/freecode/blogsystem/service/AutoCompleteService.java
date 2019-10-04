package tech.freecode.blogsystem.service;

import lombok.Data;

import java.util.List;

public interface AutoCompleteService {

    @Data
    class Item{
        private int id;
        private String text;
    }

    List<Item> autoComplete(String prefix);
}
