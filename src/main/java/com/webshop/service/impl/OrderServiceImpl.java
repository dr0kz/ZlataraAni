package com.webshop.service.impl;

import com.webshop.model.Order;
import com.webshop.model.OrderCart;
import com.webshop.model.ProductInOrderCart;
import com.webshop.model.dto.ProductQuantityDto;
import com.webshop.model.enumerations.Payment;
import com.webshop.model.exceptions.OrderCartNotFoundException;
import com.webshop.model.exceptions.OrderNotFoundException;
import com.webshop.repository.OrderCartRepository;
import com.webshop.repository.OrderRepository;
import com.webshop.service.OrderService;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderCartRepository orderCartRepository;
    private final ProductInOrderCartService productInOrderCartService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderCartRepository orderCartRepository, ProductInOrderCartService productInOrderCartService) {
        this.orderRepository = orderRepository;
        this.orderCartRepository = orderCartRepository;
        this.productInOrderCartService = productInOrderCartService;
    }


    @Override
    public List<Order> listAll() {
        return this.orderRepository.findAll();
    }

    @Override
    public List<Order> findAllBy(String filter) {
        return this.orderRepository.findAllByTotalPriceBetween(1000, 1200);
        //return this.orderRepository.findAllByDateCreatedMonth(3);
        //return this.orderRepository.findAllByClientPersonalInfo("%"+filter+"%");
    }

    @Override
    public void createOrder(String clientName, String clientSurname, String mobileNumber,
                            Payment orderType, Integer postalCode, String street, String city, String cookieValue) {
        Long id = Long.parseLong(cookieValue);
        OrderCart orderCart = this.orderCartRepository.findById(id).orElseThrow(OrderCartNotFoundException::new);

        int totalPrice = getOrderTotalPrice(this.productInOrderCartService
                .findAllProductsInOrderCart(orderCart)
                .stream()
                .map(p -> new ProductQuantityDto(p.getProduct(),p.getQuantity()))
                .collect(Collectors.toList())
        );

        Order order = new Order(totalPrice, orderType, mobileNumber, clientName, clientSurname, postalCode, street, city, LocalDateTime.now(),"");
        this.orderRepository.save(order);

    }

    private int getOrderTotalPrice(List<ProductQuantityDto> productQuantities) {
        return productQuantities.stream()
                .mapToInt(t -> t.getQuantity() * t.getProduct().getPrice())
                .sum();
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        this.orderRepository.delete(order);
    }

}
