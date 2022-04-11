package com.webshop.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class OrderCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private LocalDateTime created;

    private Boolean isAccepted;

    public OrderCart() {
        this.created = LocalDateTime.now();
        isAccepted = false;
    }
}
