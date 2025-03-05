package com.home.marketplace.controllers;

import com.home.marketplace.db.entities.OrderEntity;
import com.home.marketplace.services.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<OrderEntity>> all() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public EntityModel<OrderEntity> one(@PathVariable Long id) {
        return orderService.getOneOrder(id);
    }

    /*@PostMapping("/orders")
    ResponseEntity<EntityModel<OrderEntity>> newOrder(@RequestBody OrderEntity order) {
        return orderService.newOrder(order);
    }*/

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        return orderService.completeOrder(id);
    }
}