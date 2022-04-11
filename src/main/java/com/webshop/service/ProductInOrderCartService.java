package com.webshop.service;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;

import java.util.List;
import java.util.Optional;

public interface ProductInOrderCartService {

    ProductInOrderCart create(OrderCart orderCart, Product product, int quantity);

    List<ProductInOrderCart> findAllProductsInOrderCart(OrderCart orderCart);

    Optional<ProductInOrderCart> findByProductAndOrderCart(Product product, OrderCart orderCart);

    ProductInOrderCart updateQuantity(ProductInOrderCart productInOrderCart, int quantity);

}
