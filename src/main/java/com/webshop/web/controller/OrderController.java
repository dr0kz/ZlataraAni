package com.webshop.web.controller;

import com.webshop.model.Order;
import com.webshop.model.Product;
import com.webshop.model.enumerations.Payment;
import com.webshop.repository.OrderRepository;
import com.webshop.repository.ProductRepository;
import com.webshop.service.OrderCartService;
import com.webshop.service.OrderService;
import com.webshop.service.ProductInOrderCartService;
import com.webshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, ProductInOrderCartService productInOrderCartService, OrderCartService orderCartService, ProductService productService, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.productInOrderCartService = productInOrderCartService;
        this.orderCartService = orderCartService;
        this.productService = productService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
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
                              HttpServletResponse response,
                              Model model) {
        if (response.getHeader("COOKIE") != null) {
            // vashata koshnica e prazna
        } else if (request.getCookies() != null && Arrays.stream(request.getCookies()).anyMatch(t -> t.getName().equals("CART"))) {
            Long orderCartId = Long.parseLong(Arrays.stream(request.getCookies()).filter(t -> t.getName().equals("CART")).findFirst().get().getValue());
            if (this.productInOrderCartService.findAllProductsInOrderCart(this.orderCartService.findOrderCartById(orderCartId)).size() != 0) {
                this.orderService.createOrder(clientName, clientSurname, city, street, postalCode, email, mobileNumber, orderType, orderCartId);
            }
        }
        return "redirect:/";
    }

    @PostMapping("/accept-order/{id}")
    public String acceptOrder(@PathVariable Long id) {
        Order o = this.orderService.findById(id);
        Arrays.stream(o.getProductIds().split(" "))
                .forEach(t -> {
                    Long i = Long.parseLong(t.split("-")[0]);
                    Integer q = Integer.parseInt(t.split("-")[1]);

                    Product p =this.productService.findById(i);
                    int stocks = p.getStocks() - q;
                    if(stocks<0) stocks = 0;
                    p.setStocks(stocks);
                    this.productRepository.save(p);
                });
        o.setIsConfirmed(true);
        this.orderRepository.save(o);
        return "redirect:/admin/orders";
    }
}
