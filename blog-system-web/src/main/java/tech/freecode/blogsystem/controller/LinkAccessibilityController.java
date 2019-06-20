package tech.freecode.blogsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.freecode.blogsystem.service.LinkAccessibilityService;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LinkAccessibilityController {
    @Resource
    private LinkAccessibilityService service;

    @GetMapping("admin/link-accessibility-validation-reports")
    public List<LinkAccessibilityService.AccessibilityFailReport> getReports(){
        return service.getReports();
    }
}
