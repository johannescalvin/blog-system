package tech.freecode.blogsystem.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
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
