package com.home.marketplace.assemblers;

import com.home.marketplace.controllers.GoodsController;
import com.home.marketplace.db.entities.GoodEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GoodEntityModelAssembler implements RepresentationModelAssembler<GoodEntity, EntityModel<GoodEntity>> {

    @Override
    public EntityModel<GoodEntity> toModel(GoodEntity entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(GoodsController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(GoodsController.class).all()).withRel("goods"));
    }
}
