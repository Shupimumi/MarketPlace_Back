package com.home.marketplace.controllers;

import com.home.marketplace.assemblers.GoodEntityModelAssembler;
import com.home.marketplace.controllers.exceptions.GoodNotFoundException;
import com.home.marketplace.db.repositories.GoodsRepository;
import com.home.marketplace.db.entities.GoodEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class GoodsController {
    private final GoodsRepository goodsRepository;
    private final GoodEntityModelAssembler assembler;

    GoodsController(GoodsRepository goodsRepository, GoodEntityModelAssembler assembler){
        this.goodsRepository = goodsRepository;
        this.assembler = assembler;
    }

    @GetMapping("/goods")
    public CollectionModel<EntityModel<GoodEntity>> all(){
        List<EntityModel<GoodEntity>> goods = goodsRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(goods, linkTo(methodOn(GoodsController.class).all()).withSelfRel());
    }

    @PostMapping("/goods")
    ResponseEntity<?> newGood(@RequestBody GoodEntity goodEntity){
        EntityModel<GoodEntity> goodEntityModel = assembler.toModel(goodsRepository.save(goodEntity));

        return ResponseEntity
                .created(goodEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(goodEntityModel);
    }

    @GetMapping("/goods/{id}")
    public EntityModel<GoodEntity> one(@PathVariable Long id){
        GoodEntity goodEntity = goodsRepository.findById(id)
                .orElseThrow(() -> new GoodNotFoundException(id));

        return assembler.toModel(goodEntity);
    }

    @PutMapping("/goods/{id}")
    ResponseEntity<?> replaceGood(@RequestBody GoodEntity newGood, @PathVariable Long id){
        GoodEntity updatedGoodEntity = goodsRepository.findById(id)
                .map(goodEntity -> {
                    goodEntity.setName(newGood.getName());
                    goodEntity.setCost(newGood.getCost());
                    return goodsRepository.save(goodEntity);
                })
                .orElseGet(() -> {
                    return goodsRepository.save(newGood);
                });

        EntityModel<GoodEntity> goodEntityModel = assembler.toModel(updatedGoodEntity);

        return ResponseEntity
                .created(goodEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(goodEntityModel);
    }

    @DeleteMapping("/goods/{id}")
    ResponseEntity<?> deleteGood(@PathVariable Long id){
        goodsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
