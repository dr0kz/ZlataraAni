package com.webshop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/notFound")
    public String getPageNotFound(Model model){
        model.addAttribute("bodyContent","not-found");
        return "master-template";
    }
}
