package com.readingrover.springbootdemo.views;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/authors")
public class ManageAuthorsController
{
    @GetMapping
    public String list(Model model)
    {
        return "authors/search"; // Name of file ("template")
    }
    
    @GetMapping("/edit")
    public String edit(Model model, @Param("href") String href)
    {
        model.addAttribute("href", href);
        return "authors/edit"; // Name of file ("template")
    }
}
