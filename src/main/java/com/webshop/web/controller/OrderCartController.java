package com.webshop.web.controller;

import com.webshop.model.ProductInOrderCart;
import com.webshop.service.OrderCartService;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/orderCart")
public class OrderCartController {

    private final OrderCartService orderCartService;
    private final ProductInOrderCartService productInOrderCartService;

    public OrderCartController(OrderCartService orderCartService, ProductInOrderCartService productInOrderCartService) {
        this.orderCartService = orderCartService;
        this.productInOrderCartService = productInOrderCartService;
    }

    @PostMapping("/add")
    public String addProductToCart(HttpServletRequest request, @RequestParam Long productId, @RequestParam int quantity, HttpServletResponse response) {
        if (response.getHeader("COOKIE") != null) {
            Long orderCartId = Long.parseLong(response.getHeader("COOKIE"));
            this.orderCartService
                    .addProductToOrderCart(productId, orderCartId, quantity);
        } else if (request.getCookies() != null && Arrays.stream(request.getCookies()).anyMatch(t -> t.getName().equals("CART"))) {
            Long orderCartId = Long.parseLong(Arrays.stream(request.getCookies()).filter(t -> t.getName().equals("CART")).findFirst().get().getValue());
            this.orderCartService
                    .addProductToOrderCart(productId, orderCartId, quantity);
        }

        return "redirect:/proizvodi/" + productId;
    }

    @GetMapping("/shop-checkout")
    public String getShopCheckoutPage(Model model) {
        model.addAttribute("bodyContent", "shop-catalog-sidebar");
        return "master-template";
    }

    @GetMapping
    public String getShoppingCartPage(Model model, @Nullable ModelAndView modelAndView) {
        model.addAttribute("bodyContent", "shop-cart");
        return "master-template";
    }

}
