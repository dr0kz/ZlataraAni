package com.webshop.service.impl;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;
import com.webshop.repository.ProductInOrderCartRepository;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductInOrderCartServiceImpl implements ProductInOrderCartService {

    private final ProductInOrderCartRepository productInOrderCartRepository;

    public ProductInOrderCartServiceImpl(ProductInOrderCartRepository productInOrderCartRepository) {
        this.productInOrderCartRepository = productInOrderCartRepository;
    }

    @Override
    public ProductInOrderCart create(OrderCart orderCart, Product product, int quantity) {
        return this.productInOrderCartRepository.save(new ProductInOrderCart(orderCart, product, quantity));
    }

    @Override
    public List<ProductInOrderCart> findAllProductsInOrderCart(OrderCart orderCart) {
        return this.productInOrderCartRepository.findAllByOrderCart(orderCart);
    }

    @Override
    public Optional<ProductInOrderCart> findByProductAndOrderCart(Product product, OrderCart orderCart) {
        return this.productInOrderCartRepository.findProductInOrderCartByProductAndOrderCart(product, orderCart);
    }

    @Override
    public ProductInOrderCart updateQuantity(ProductInOrderCart productInOrderCart, int quantity) {
        int currentQuantity = productInOrderCart.getQuantity();
        productInOrderCart.setQuantity(currentQuantity + quantity);
        return this.productInOrderCartRepository.save(productInOrderCart);
    }
}
