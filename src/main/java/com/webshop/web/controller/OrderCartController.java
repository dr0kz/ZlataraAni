package com.webshop.web.controller;

import com.webshop.model.OrderCart;
import com.webshop.service.OrderCartService;
import org.postgresql.jdbc.PreferQueryMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Controller
@RequestMapping("/orderCart")
public class OrderCartController {

    private final OrderCartService orderCartService;

    public OrderCartController(OrderCartService orderCartService) {
        this.orderCartService = orderCartService;
    }

    @PostMapping("/add")
    public String addProductToCart(HttpServletRequest request, @RequestParam Long productId, @RequestParam int quantity, HttpServletResponse response){
        if(response.getHeader("COOKIE")!=null){
            Long orderCartId = Long.parseLong(response.getHeader("COOKIE"));
            this.orderCartService
                    .addProductToOrderCart(productId, orderCartId, quantity);
        }else if(request.getCookies()!=null && Arrays.stream(request.getCookies()).anyMatch(t -> t.getName().equals("CART"))){
            Long orderCartId = Long.parseLong(Arrays.stream(request.getCookies()).filter(t -> t.getName().equals("CART")).findFirst().get().getValue());
            this.orderCartService
                    .addProductToOrderCart(productId, orderCartId, quantity);
        }

        return "redirect:/proizvodi/"+productId;
    }

    @GetMapping("/shop-checkout")
    public String getShopCheckoutPage(Model model){
        model.addAttribute("bodyContent","shop-catalog-sidebar");
        return "master-template";
    }

    @GetMapping
    public String getShoppingCartPage(Model model){
        model.addAttribute("bodyContent","shop-cart");
        return "master-template";
    }

}
