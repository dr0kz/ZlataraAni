package com.webshop.service;

import com.webshop.model.Order;
import com.webshop.model.OrderCart;
import com.webshop.model.ProductInOrderCart;
import com.webshop.model.dto.ProductQuantityDto;
import com.webshop.model.enumerations.Payment;
import com.webshop.model.exceptions.OrderCartNotFoundException;
import com.webshop.model.exceptions.OrderNotFoundException;
import com.webshop.repository.OrderCartRepository;
import com.webshop.repository.OrderRepository;
import com.webshop.repository.ProductInOrderCartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCartRepository orderCartRepository;
    private final ProductInOrderCartService productInOrderCartService;
    private final ProductInOrderCartRepository productInOrderCartRepository;

    public OrderService(OrderRepository orderRepository, OrderCartRepository orderCartRepository, ProductInOrderCartService productInOrderCartService, ProductInOrderCartRepository productInOrderCartRepository) {
        this.orderRepository = orderRepository;
        this.orderCartRepository = orderCartRepository;
        this.productInOrderCartService = productInOrderCartService;
        this.productInOrderCartRepository = productInOrderCartRepository;
    }


    public List<Order> listAll() {
        return this.orderRepository.findAll();
    }

    public List<Order> findAllBy(String filter) {
        return this.orderRepository.findAllByTotalPriceBetween(1000, 1200);
        //return this.orderRepository.findAllByDateCreatedMonth(3);
        //return this.orderRepository.findAllByClientPersonalInfo("%"+filter+"%");
    }

    public Order findById(Long id){
        return this.orderRepository.findById(id).get();
    }

    public boolean createOrder(String clientName,
                            String clientSurname,
                            String city,
                            String street,
                            String postalCode,
                            String email,
                            String mobileNumber,
                            Payment orderType,
                            Long orderCartId) {
        OrderCart orderCart = this.orderCartRepository.findById(orderCartId).orElseThrow(OrderCartNotFoundException::new);

        List<ProductInOrderCart> productsInOrderCart = this.productInOrderCartService
                .findAllProductsInOrderCart(orderCart);

        boolean noStock = productsInOrderCart.stream().anyMatch(productInOrderCart -> productInOrderCart.getProduct().getStocks() <= 0);

        if (noStock) {
            productsInOrderCart = productsInOrderCart.stream().filter(productInOrderCart -> productInOrderCart.getProduct().getStocks() <= 0).collect(Collectors.toList());
            this.productInOrderCartRepository.deleteAll(productsInOrderCart);
            return false;
        }

        int totalPrice = getOrderTotalPrice(productsInOrderCart.stream()
                .map(p -> new ProductQuantityDto(p.getProduct(), p.getQuantity()))
                .collect(Collectors.toList())
        );
        if(totalPrice<1000){
            totalPrice+=130;
        }

        List<ProductInOrderCart> t = this.productInOrderCartService.findAllProductsInOrderCart(orderCart);

        String products = t
                .stream()
                .map(product -> product.getProduct().getName() + " - " + product.getQuantity() + " x " + product.getProduct().calculateDiscount() + " ден.")
                .collect(Collectors.joining("\n"));

        String productIds = t.stream()
                .map(k -> k.getProduct().getId().toString()+"-"+k.getQuantity())
                .collect(Collectors.joining(" "));

        Order order = new Order(totalPrice, orderType, mobileNumber, clientName, clientSurname, postalCode, street, city, LocalDateTime.now(), products, email, productIds);
        this.orderRepository.save(order);
        this.productInOrderCartService.deleteOrderCart(orderCart);
        return true;
    }

    private int getOrderTotalPrice(List<ProductQuantityDto> productQuantities) {
        return productQuantities.stream()
                .mapToInt(t -> t.getQuantity() * t.getProduct().calculateDiscount())
                .sum();
    }

    public void deleteOrder(Long orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        this.orderRepository.delete(order);
    }

}
