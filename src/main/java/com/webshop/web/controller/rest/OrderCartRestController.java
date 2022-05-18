package com.webshop.web.controller.rest;

import com.webshop.model.dto.OrderCartProductsDto;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/api/orderCart")
public class OrderCartRestController {

    private final ProductInOrderCartService productInOrderCartService;

    public OrderCartRestController(ProductInOrderCartService productInOrderCartService) {
        this.productInOrderCartService = productInOrderCartService;
    }


    @PostMapping("/update")
    public void updateOrderCart(@RequestBody OrderCartProductsDto orderCartProductsDto, HttpServletRequest request, HttpServletResponse response) {
        if (response.getHeader("COOKIE") != null) {
            Long orderCartId = Long.parseLong(response.getHeader("COOKIE"));
            this.productInOrderCartService.updateAllProductsInOrderCart(orderCartProductsDto, orderCartId);
        } else if (request.getCookies() != null && Arrays.stream(request.getCookies()).anyMatch(t -> t.getName().equals("CART"))) {
            Long orderCartId = Long.parseLong(Arrays.stream(request.getCookies()).filter(t -> t.getName().equals("CART")).findFirst().get().getValue());
            this.productInOrderCartService.updateAllProductsInOrderCart(orderCartProductsDto, orderCartId);
        }
    }

}
