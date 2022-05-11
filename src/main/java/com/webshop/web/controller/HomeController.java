package com.webshop.web.controller;

import com.webshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
    @GetMapping("/politika-na-privatnost")
    public String getPrivacyPolicyPage(Model model){
        model.addAttribute("bodyContent","politika-na-privatnost");
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
    @GetMapping("/dzoke1")
    public String getPageDzoke1(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("bodyContent","shop-catalog.html");
        return "master-template";
    }
    @GetMapping("/dzoke2")
    public String getPageDzoke2(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("bodyContent","shop-catalog-sidebar");
        return "master-template";
    }

}
