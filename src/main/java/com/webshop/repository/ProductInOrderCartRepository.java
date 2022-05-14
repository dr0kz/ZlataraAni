package com.webshop.repository;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInOrderCartRepository extends JpaRepository<ProductInOrderCart, Long>{

    List<ProductInOrderCart> findAllByOrderCart(OrderCart orderCart);

    Optional<ProductInOrderCart> findProductInOrderCartByProductAndOrderCart(Product product, OrderCart orderCart);

    void deleteAllByOrderCart(OrderCart orderCart);

}
