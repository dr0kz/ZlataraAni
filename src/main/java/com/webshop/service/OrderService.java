package com.webshop.service;

import com.webshop.model.Order;
import com.webshop.model.OrderCart;
import com.webshop.model.dto.ProductQuantityDto;
import com.webshop.model.enumerations.Payment;
import com.webshop.model.exceptions.OrderCartNotFoundException;
import com.webshop.model.exceptions.OrderNotFoundException;
import com.webshop.repository.OrderCartRepository;
import com.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCartRepository orderCartRepository;
    private final ProductInOrderCartService productInOrderCartService;

    public OrderService(OrderRepository orderRepository, OrderCartRepository orderCartRepository, ProductInOrderCartService productInOrderCartService) {
        this.orderRepository = orderRepository;
        this.orderCartRepository = orderCartRepository;
        this.productInOrderCartService = productInOrderCartService;
    }


    public List<Order> listAll() {
        return this.orderRepository.findAll();
    }

    public List<Order> findAllBy(String filter) {
        return this.orderRepository.findAllByTotalPriceBetween(1000, 1200);
        //return this.orderRepository.findAllByDateCreatedMonth(3);
        //return this.orderRepository.findAllByClientPersonalInfo("%"+filter+"%");
    }

    public void createOrder(String clientName,
                            String clientSurname,
                            String city,
                            String street,
                            String postalCode,
                            String email,
                            String mobileNumber,
                            Payment orderType,
                            Long orderCartId) {
        OrderCart orderCart = this.orderCartRepository.findById(orderCartId).orElseThrow(OrderCartNotFoundException::new);

        int totalPrice = getOrderTotalPrice(this.productInOrderCartService
                .findAllProductsInOrderCart(orderCart)
                .stream()
                .map(p -> new ProductQuantityDto(p.getProduct(), p.getQuantity()))
                .collect(Collectors.toList())
        );

        String products = this.productInOrderCartService.findAllProductsInOrderCart(orderCart)
                .stream()
                .map(product -> product.getProduct().getName() + " - " + product.getQuantity() + " x " + product.getProduct().getPrice() + " ден.")
                .collect(Collectors.joining("\n"));

        Order order = new Order(totalPrice, orderType, mobileNumber, clientName, clientSurname, postalCode, street, city, LocalDateTime.now(), products, email);
        this.orderRepository.save(order);

    }

    private int getOrderTotalPrice(List<ProductQuantityDto> productQuantities) {
        return productQuantities.stream()
                .mapToInt(t -> t.getQuantity() * t.getProduct().getPrice())
                .sum();
    }

    public void deleteOrder(Long orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        this.orderRepository.delete(order);
    }

}
