package com.webshop.web.controller;

import com.webshop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/dzoke")
public class TestController {

    private final ProductRepository productRepository;

    public TestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String getPage(HttpServletRequest request, HttpServletResponse response, Model model){
//        Cookie[] cookies = request.getCookies();
//        if(Arrays.stream(request.getCookies()).noneMatch(t -> t.getName().equals("order")))
//        {
//            Cookie cookie = new Cookie("order","123123123");
//            cookie.setMaxAge(20);
//            response.addCookie(cookie);
//        }
        //                                     tuka stavi ime na template so sakas da go vidis
        //i na localhost:8080/dzoke ke ti se otvori
        model.addAttribute("bodyContent","shop-catalog-sidebar");
//        model.addAttribute("product", this.productRepository.findById(1L).get());
        return "master-template";
    }
}
