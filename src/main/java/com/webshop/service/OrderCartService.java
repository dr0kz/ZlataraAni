package com.webshop.service;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;
import com.webshop.model.exceptions.OrderCartNotFoundException;
import com.webshop.model.exceptions.ProductNotFoundException;
import com.webshop.repository.OrderCartRepository;
import com.webshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderCartService {

    private final OrderCartRepository orderCartRepository;
    private final ProductRepository productRepository;
    private final ProductInOrderCartService productInOrderCartService;

    public OrderCartService(OrderCartRepository orderCartRepository, ProductRepository productRepository, ProductInOrderCartService productInOrderCartService) {
        this.orderCartRepository = orderCartRepository;
        this.productRepository = productRepository;
        this.productInOrderCartService = productInOrderCartService;
    }


    public OrderCart findOrderCartById(Long id) {
        return orderCartRepository.findById(id)
                .orElseThrow(OrderCartNotFoundException::new);
    }

    public List<ProductInOrderCart> addProductToOrderCart(Long productId, Long orderCartId, int quantity) {
        try {
            OrderCart orderCart = this.findOrderCartById(orderCartId);
            Product product = this.productRepository.findById(productId).get();
            Optional<ProductInOrderCart> productInOrderCart = this.productInOrderCartService.findByProductAndOrderCart(product, orderCart);
            if (productInOrderCart.isEmpty()) {
                if(quantity > product.getStocks())
                    this.productInOrderCartService.create(orderCart, product, product.getStocks());
                else
                    this.productInOrderCartService.create(orderCart,product,quantity);
            }
            else {
                this.productInOrderCartService.updateQuantity(productInOrderCart.get(), quantity);
            }
            return this.productInOrderCartService.findAllProductsInOrderCart(orderCart);
        } catch (OrderCartNotFoundException | ProductNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public OrderCart saveOrderCart(OrderCart orderCart) {
        return this.orderCartRepository.save(orderCart);
    }

    public void removeOrderCartsOlderThen(Integer days) {
        this.orderCartRepository.deleteAllByCreatedIsLessThan(LocalDateTime.now().minusDays(days));
    }

    public void acceptCookie(Long id){
        OrderCart orderCart = this.orderCartRepository.findById(id).get();
        orderCart.setIsAccepted(true);
        this.orderCartRepository.save(orderCart);
    }
}
