package com.webshop.model.exceptions;

public class OrderCartNotFoundException extends RuntimeException {
    public OrderCartNotFoundException() {
        super("OrderCart was not found!");
    }
}
