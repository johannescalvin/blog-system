package tech.freecode.blogsystem.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@ConditionalOnProperty(name = "blog-system.link-accessibility-validator.enabled",havingValue = "true")
public class LinkAccessibilityService {

    public static class AccessibilityFailReport {
        private String date;
        private String filename;
        private List<String> msg;

        public AccessibilityFailReport(String date, String filename, List<String> msg) {
            this.date = date;
            this.filename = filename;
            this.msg = msg;
        }

        public String getDate() {
            return date;
        }

        public String getFilename() {
            return filename;
        }

        public List<String> getMsg() {
            return msg;
        }
    }

    private List<AccessibilityFailReport> reports = new ArrayList<>();

    public List<AccessibilityFailReport> getReports(){
        return reports;
    }

    public void addReport(AccessibilityFailReport report){
        reports.add(report);
    }

    public synchronized void clear(){
        reports = new ArrayList<>();
    }

}
