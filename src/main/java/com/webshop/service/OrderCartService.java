package com.webshop.service;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;

import java.util.List;


public interface OrderCartService {

    OrderCart findOrderCartById(Long id);

    List<ProductInOrderCart> addProductToOrderCart(Long productId, Long orderCartId, int quantity);

    OrderCart saveOrderCart(OrderCart orderCart);

    void removeOrderCartsOlderThen(Integer interval);

    void acceptCookie(Long id);

}
