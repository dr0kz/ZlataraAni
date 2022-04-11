package com.webshop.repository;

import com.webshop.model.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface OrderCartRepository extends JpaRepository<OrderCart,Long> {
    @Transactional
    void deleteAllByCreatedIsLessThan(LocalDateTime date);
}
