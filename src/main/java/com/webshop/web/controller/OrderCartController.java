package com.webshop.web.controller;

import com.webshop.service.OrderCartService;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

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
    public String addProductToCart(HttpServletRequest request, @RequestParam Long productId, @RequestParam int quantity, HttpServletResponse response) throws MalformedURLException, URISyntaxException {

        System.out.println(request.getAttribute("javax.servlet.forward.request_uri"));

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
