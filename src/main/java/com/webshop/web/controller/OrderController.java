package com.webshop.web.controller;

import com.webshop.model.OrderCart;
import com.webshop.model.ProductInOrderCart;
import com.webshop.model.enumerations.Payment;
import com.webshop.service.OrderCartService;
import com.webshop.service.OrderService;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductInOrderCartService productInOrderCartService;
    private final OrderCartService orderCartService;
    public OrderController(OrderService orderService, ProductInOrderCartService productInOrderCartService, OrderCartService orderCartService) {
        this.orderService = orderService;
        this.productInOrderCartService = productInOrderCartService;
        this.orderCartService = orderCartService;
    }

//    @GetMapping("/list")
//    public String ordersList(@RequestParam(required = false) String filter, Model model){
//        if(filter!=null && !filter.equals("")){
//            model.addAttribute("orders", this.orderService.findAllBy(filter));
//        }else{
//            model.addAttribute("orders", this.orderService.listAll());
//        }
//        return "home";
//    }

    @PostMapping
    public String createOrder(String clientName, String clientSurname, String mobileNumber,
                              Payment orderType, Integer postalCode, String street, String city,
                              HttpServletRequest request){
        String cookieValue = Arrays.stream(request.getCookies()).filter(cookie1 -> cookie1.getName().equals("CART")).findFirst().get().getValue();
        this.orderService.createOrder(clientName, clientSurname, mobileNumber, orderType, postalCode, street, city, cookieValue);
        return "home";
    }
}
