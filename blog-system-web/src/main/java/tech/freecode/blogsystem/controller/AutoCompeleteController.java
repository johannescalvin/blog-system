package tech.freecode.blogsystem.controller;

import org.springframework.web.bind.annotation.*;
import tech.freecode.blogsystem.service.AutoCompleteService;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AutoCompeleteController {
    @Resource
    private AutoCompleteService autoCompleteService;

    @GetMapping("/auto_complete")
    public List<AutoCompleteService.Item> autoComplete(@RequestParam(value = "prefix",required = false) String prefix){
        return  autoCompleteService.autoComplete(prefix);
    }
}
