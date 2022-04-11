package com.webshop.model;

import com.webshop.model.composite.key.ProductOrderCart;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
public class ProductInOrderCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderCart_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OrderCart orderCart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public ProductInOrderCart() {
    }

    public ProductInOrderCart(OrderCart orderCart, Product product, Integer quantity) {
        this.orderCart = orderCart;
        this.product = product;
        this.quantity = quantity;
    }
}
