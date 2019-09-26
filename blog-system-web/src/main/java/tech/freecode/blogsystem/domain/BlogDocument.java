package tech.freecode.blogsystem.domain;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Document(indexName = "blog",type = "blog")
@Data
public class BlogDocument implements Serializable {
    @Id
    @JsonIgnore
    private String id;

    private String link;

    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String title;

    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private Set<String> authors;

    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private Set<String> subTitles;
    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private Set<String> keywords;
    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String description;

    @JsonIgnore
    private Set<String> codes;

    @JsonIgnore
    @Field(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String fulltext;

    private Date createdTime;

    private Date modifiedTime;

    private long visitedTimes;
    private int updatedTimes;

}
