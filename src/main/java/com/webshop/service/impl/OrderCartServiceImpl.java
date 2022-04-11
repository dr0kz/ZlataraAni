package com.webshop.service.impl;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;
import com.webshop.model.exceptions.CategoryNotFoundException;
import com.webshop.model.exceptions.OrderCartNotFoundException;
import com.webshop.model.exceptions.ProductNotFoundException;
import com.webshop.repository.OrderCartRepository;
import com.webshop.service.OrderCartService;
import com.webshop.service.ProductInOrderCartService;
import com.webshop.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderCartServiceImpl implements OrderCartService {

    private final OrderCartRepository orderCartRepository;
    private final ProductService productService;
    private final ProductInOrderCartService productInOrderCartService;

    public OrderCartServiceImpl(OrderCartRepository orderCartRepository, ProductService productService, ProductInOrderCartService productInOrderCartService) {
        this.orderCartRepository = orderCartRepository;
        this.productService = productService;
        this.productInOrderCartService = productInOrderCartService;
    }


    @Override
    public OrderCart findOrderCartById(Long id) {
        return orderCartRepository.findById(id).orElseThrow(OrderCartNotFoundException::new);
    }

    @Override
    public List<ProductInOrderCart> addProductToOrderCart(Long productId, Long orderCartId, int quantity) {
        try {
            OrderCart orderCart = this.findOrderCartById(orderCartId);
            Product product = this.productService.findById(productId);
            Optional<ProductInOrderCart> productInOrderCart = this.productInOrderCartService.findByProductAndOrderCart(product, orderCart);
            if (productInOrderCart.isEmpty()) {
                this.productInOrderCartService.create(orderCart, product, quantity);
            }
            else {
                this.productInOrderCartService.updateQuantity(productInOrderCart.get(), quantity);
            }
            return this.productInOrderCartService.findAllProductsInOrderCart(orderCart);
        } catch (OrderCartNotFoundException | ProductNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public OrderCart saveOrderCart(OrderCart orderCart) {
        return this.orderCartRepository.save(orderCart);
    }

    @Override
    public void removeOrderCartsOlderThen(Integer days) {
        this.orderCartRepository.deleteAllByCreatedIsLessThan(LocalDateTime.now().minusDays(days));
    }

    @Override
    public void acceptCookie(Long id){
        OrderCart orderCart = this.orderCartRepository.findById(id).get();
        orderCart.setIsAccepted(true);
        this.orderCartRepository.save(orderCart);
    }
}
