package com.webshop.repository;

import com.webshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select * from orders where city ilike ?1 " +
            " or client_name ilike ?1" +
            " or client_surname ilike ?1" ,nativeQuery = true)
    List<Order> findAllByClientPersonalInfo(String filter);

    @Query(value = "select * from orders where extract(month from date_created) = ?1",nativeQuery = true)
    List<Order> findAllByDateCreatedMonth(Integer month);

    List<Order> findAllByTotalPriceBetween(Integer from, Integer to);

}
