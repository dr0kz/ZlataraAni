package com.webshop.model;

import com.webshop.model.enumerations.Payment;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Payment orderType;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientSurname;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String products;

    @Column(nullable = false)
    private String email;

    private String productIds;

    private Boolean isConfirmed;


    public Order() {
    }

    public Order(Integer totalPrice, Payment orderType, String mobileNumber, String clientName,
                 String clientSurname, String postalCode, String street,
                 String city, LocalDateTime dateCreated, String products, String email, String productIds) {
        this.totalPrice = totalPrice;
        this.orderType = orderType;
        this.mobileNumber = mobileNumber;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.postalCode = postalCode;
        this.street = street;
        this.city = city;
        this.dateCreated = dateCreated;
        this.products = products;
        this.email = email;
        this.productIds = productIds;
        this.isConfirmed = false;
    }
}
