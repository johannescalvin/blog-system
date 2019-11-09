package tech.freecode.blogsystem.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.freecode.blogsystem.service.LinkAccessibilityService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@ConditionalOnProperty(name = "blog-system.link-accessibility-validator.enabled",havingValue = "true")
public class LinkAccessibilityController {
    @Resource
    private LinkAccessibilityService service;

    @GetMapping("/admin/link-accessibility-validation-reports")
    public List<LinkAccessibilityService.AccessibilityFailReport> getReports(){
        return service.getReports();
    }
}
