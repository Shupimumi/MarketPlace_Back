package com.home.marketplace.services;

import com.home.marketplace.assemblers.OrderModelAssembler;
import com.home.marketplace.controllers.OrderController;
import com.home.marketplace.controllers.exceptions.GoodsForOrderNotFoundException;
import com.home.marketplace.controllers.exceptions.OrderNotFoundException;
import com.home.marketplace.db.entities.GoodEntity;
import com.home.marketplace.db.entities.OrderEntity;
import com.home.marketplace.db.repositories.OrderRepository;
import com.home.marketplace.enums.Status;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderModelAssembler assembler;
    private final GoodsService goodsService;

    OrderService(OrderRepository orderRepository, OrderModelAssembler assembler, GoodsService goodsService) {
        this.goodsService = goodsService;
        this.orderRepository = orderRepository;
        this.assembler = assembler;
    }

    public CollectionModel<EntityModel<OrderEntity>> getAllOrders() {
        List<EntityModel<OrderEntity>> orders = orderRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    public EntityModel<OrderEntity> getOneOrder(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @Transactional
    public ResponseEntity<EntityModel<OrderEntity>> newOrder(List<Long> goodsIds, String description) {
        List<GoodEntity> goods = goodsService.getAllGoodsById(goodsIds);
        if (goods.isEmpty()) {
            throw new GoodsForOrderNotFoundException();
        }
        var goodEntitiesIds = goods.stream().map(GoodEntity::getId).toList();
        var missedIds = goodsIds.stream().filter(id -> !goodEntitiesIds.contains(id)).toList();
        if (!missedIds.isEmpty()) {
            throw new GoodsForOrderNotFoundException();
        }
        OrderEntity order = new OrderEntity();
        order.setStatus(Status.IN_PROGRESS);
        order.setDescription(description);
        order.setGoods(goods);
        OrderEntity newOrder = orderRepository.save(order);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
                .body(assembler.toModel(newOrder));
    }

    public ResponseEntity<?> cancelOrder(Long id) {

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    public ResponseEntity<?> completeOrder(Long id) {

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
    }
}
