package com.home.marketplace.controllers.exceptions;

public class GoodNotFoundException extends RuntimeException {
    public GoodNotFoundException(Long id) {
        super("Couldn't find good " + id);
    }
}
