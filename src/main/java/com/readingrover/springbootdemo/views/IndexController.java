package com.readingrover.springbootdemo.views;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController
{
    @Value("${spring.application.name}")
    private String app_name;
        
    @GetMapping("/")
    public String index(Model model)
    {
        model.addAttribute("app_name", app_name);
        return "index"; // Name of file ("template")
    }
}
