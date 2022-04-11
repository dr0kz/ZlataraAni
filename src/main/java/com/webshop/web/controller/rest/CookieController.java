package com.webshop.web.controller.rest;

import com.webshop.service.OrderCartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/api/cookies")
public class CookieController {


    private final OrderCartService orderCartService;

    public CookieController(OrderCartService orderCartService) {
        this.orderCartService = orderCartService;
    }

    @PostMapping("/accept-cookies")
    public void acceptCookie(HttpServletRequest request, HttpServletResponse response){
        if(response.getHeader("COOKIE")!=null){
            Long orderCartId = Long.parseLong(response.getHeader("COOKIE"));
            this.orderCartService.acceptCookie(orderCartId);
        }else if(request.getCookies()!=null && Arrays.stream(request.getCookies()).anyMatch(t -> t.getName().equals("CART"))){
            Long orderCartId = Long.parseLong(Arrays.stream(request.getCookies()).filter(t -> t.getName().equals("CART")).findFirst().get().getValue());
            this.orderCartService.acceptCookie(orderCartId);
        }
    }
}
