package com.home.marketplace.controllers;

import com.home.marketplace.db.entities.Order;
import com.home.marketplace.db.entities.controllerdto.CreateOrderBody;
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
    public CollectionModel<EntityModel<Order>> all() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {
        return orderService.getOneOrder(id);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody CreateOrderBody order) {
        return orderService.newOrder(order.getGoodsId(), order.getDescription());
    }
    
    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        return orderService.completeOrder(id);
    }
}