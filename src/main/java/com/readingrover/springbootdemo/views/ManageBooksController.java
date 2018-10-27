package com.readingrover.springbootdemo.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/books")
public class ManageBooksController
{
    @GetMapping
    public String list(Model model)
    {
        return "books/search"; // Name of file ("template")
    }
    
    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable Long id)
    {
        model.addAttribute("id", id);
        return "books/edit"; // Name of file ("template")
    }
}
