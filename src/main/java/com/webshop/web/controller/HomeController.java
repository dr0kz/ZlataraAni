package com.webshop.web.controller;

import com.webshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getHome(Model model){
        model.addAttribute("bodyContent","home");
        model.addAttribute("discountProducts", this.productService.findAllByIsOnDiscount());
        model.addAttribute("dealOfTheDayProducts", this.productService.findAllDealOfTheDayProducts());
        return "master-template";
    }

    @GetMapping("/politika-na-kolacinja")
    public String getCookiesPolicyPage(Model model){
        model.addAttribute("bodyContent","politika-na-kolacinja");
        return "master-template";
    }
    @GetMapping("/za-nas")
    public String getAboutUsPage(Model model){
        model.addAttribute("bodyContent","about-us");
        return "master-template";
    }

    @GetMapping("/kontakt")
    public String getContactPage(Model model){
        model.addAttribute("bodyContent","contact");
        return "master-template";
    }
}
