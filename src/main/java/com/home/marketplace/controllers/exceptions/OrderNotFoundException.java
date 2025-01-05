package com.home.marketplace.controllers.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Couldn't find order " + id);
    }
}
