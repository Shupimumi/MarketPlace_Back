package com.home.marketplace.assemblers;

import com.home.marketplace.controllers.OrderController;
import com.home.marketplace.db.entities.CustomerOrder;
import com.home.marketplace.enums.Status;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<CustomerOrder, EntityModel<CustomerOrder>> {

    @Override
    public EntityModel<CustomerOrder> toModel(CustomerOrder customerOrder) {
        // Unconditional links to single-item resource and aggregate root

        EntityModel<CustomerOrder> orderModel = EntityModel.of(customerOrder,
                linkTo(methodOn(OrderController.class).one(customerOrder.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders"));

        // Conditional links based on state of the order

        if (customerOrder.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(customerOrder.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(customerOrder.getId())).withRel("complete"));
        }

        return orderModel;
    }
}
