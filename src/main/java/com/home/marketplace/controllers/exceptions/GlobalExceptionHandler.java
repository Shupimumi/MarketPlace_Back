package com.home.marketplace.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GoodNotFoundException.class)
    public static ResponseEntity<Object> goodNotFoundException(GoodNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse("GOOD_NOT_FOUND", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
