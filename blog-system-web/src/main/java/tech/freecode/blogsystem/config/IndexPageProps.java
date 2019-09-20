package tech.freecode.blogsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "blog-system.index-page")
public class IndexPageProps {
    private String displayTitle;
    private String seoKeywordsZh;
    private String getSeoKeywordsEn;
    private List<String> seoDescriptions;
    private String recordCode;
    private String contact;

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getSeoKeywordsZh() {
        return seoKeywordsZh;
    }

    public void setSeoKeywordsZh(String seoKeywordsZh) {
        this.seoKeywordsZh = seoKeywordsZh;
    }

    public String getGetSeoKeywordsEn() {
        return getSeoKeywordsEn;
    }

    public void setGetSeoKeywordsEn(String getSeoKeywordsEn) {
        this.getSeoKeywordsEn = getSeoKeywordsEn;
    }

    public List<String> getSeoDescriptions() {
        return seoDescriptions;
    }

    public void setSeoDescriptions(List<String> seoDescriptions) {
        this.seoDescriptions = seoDescriptions;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
