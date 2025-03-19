package com.home.marketplace.controllers.exceptions;

public class GoodsForOrderNotFoundException extends RuntimeException {
    public GoodsForOrderNotFoundException() {
        super("Couldn't find all goods for order");
    }
}
