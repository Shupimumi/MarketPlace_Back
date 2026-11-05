package com.home.marketplace.assemblers;

import com.home.marketplace.controllers.GoodsController;
import com.home.marketplace.db.entities.Good;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GoodEntityModelAssembler implements RepresentationModelAssembler<Good, EntityModel<Good>> {

    @Override
    public EntityModel<Good> toModel(Good entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(GoodsController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(GoodsController.class).all()).withRel("goods"));
    }
}
