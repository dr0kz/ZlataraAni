package com.webshop.web.controller;

import com.webshop.model.enumerations.Payment;
import com.webshop.service.OrderCartService;
import com.webshop.service.OrderService;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

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
    public String createOrder(String clientName,
                              String clientSurname,
                              String city,
                              String street,
                              String postalCode,
                              String email,
                              String mobileNumber,
                              Payment orderType,
                              HttpServletRequest request,
                              HttpServletResponse response){
        if(response.getHeader("COOKIE")!=null){
            // vashata koshnica e prazna
        }else if(request.getCookies()!=null && Arrays.stream(request.getCookies()).anyMatch(t -> t.getName().equals("CART"))){
            Long orderCartId = Long.parseLong(Arrays.stream(request.getCookies()).filter(t -> t.getName().equals("CART")).findFirst().get().getValue());
            this.orderService.createOrder(clientName,clientSurname,city,street,postalCode,email,mobileNumber,orderType, orderCartId);
        }
        return "home";
    }
}
