package com.webshop.model.composite.key;

import java.io.Serializable;
import java.util.Objects;

public class ProductOrderCart implements Serializable {
    private Long product;
    private Long orderCart;

    public ProductOrderCart(Long product, Long orderCart) {
        this.product = product;
        this.orderCart = orderCart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductOrderCart)) return false;
        ProductOrderCart that = (ProductOrderCart) o;
        return Objects.equals(product, that.product) && Objects.equals(orderCart, that.orderCart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, orderCart);
    }
}
