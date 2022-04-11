package com.webshop.service;


import com.webshop.model.Order;
import com.webshop.model.dto.ProductQuantityDto;
import com.webshop.model.enumerations.Payment;
import java.util.List;

public interface OrderService {
    List<Order> listAll();
    List<Order> findAllBy(String filter);
    void createOrder(String clientName, String clientSurname, String mobileNumber,
                     Payment orderType, Integer postalCode, String street, String city,
                     String cookieValue);
    void deleteOrder(Long orderId);

}
