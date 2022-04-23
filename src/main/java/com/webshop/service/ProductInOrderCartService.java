package com.webshop.service;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;
import com.webshop.repository.ProductInOrderCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductInOrderCartService {

    private final ProductInOrderCartRepository productInOrderCartRepository;

    public ProductInOrderCartService(ProductInOrderCartRepository productInOrderCartRepository) {
        this.productInOrderCartRepository = productInOrderCartRepository;
    }

    public ProductInOrderCart create(OrderCart orderCart, Product product, int quantity) {
        return this.productInOrderCartRepository.save(new ProductInOrderCart(orderCart, product, quantity));
    }

    public List<ProductInOrderCart> findAllProductsInOrderCart(OrderCart orderCart) {
        return this.productInOrderCartRepository.findAllByOrderCart(orderCart);
    }

    public Optional<ProductInOrderCart> findByProductAndOrderCart(Product product, OrderCart orderCart) {
        return this.productInOrderCartRepository.findProductInOrderCartByProductAndOrderCart(product, orderCart);
    }

    public ProductInOrderCart updateQuantity(ProductInOrderCart productInOrderCart, int quantity) {
        int currentQuantity = productInOrderCart.getQuantity();
        productInOrderCart.setQuantity(currentQuantity + quantity);
        return this.productInOrderCartRepository.save(productInOrderCart);
    }
}
