package com.home.marketplace.controllers;

import com.home.marketplace.assemblers.GoodEntityModelAssembler;
import com.home.marketplace.db.entities.GoodEntity;
import com.home.marketplace.services.GoodsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GoodsController {
    @PersistenceContext
    private EntityManager entityManager;
    private final GoodsService goodsService;
    private final GoodEntityModelAssembler assembler;

    @Autowired
    GoodsController(GoodsService goodsService, GoodEntityModelAssembler assembler) {
        this.assembler = assembler;
        this.goodsService = goodsService;
    }

    @GetMapping("/goods")
    public ResponseEntity<?> all() {
        return goodsService.getAllGoods();
    }

    @PostMapping("/goods")
    @Transactional
    public ResponseEntity<?> newGood(@RequestBody GoodEntity goodEntity) {
        GoodEntity createdGood = goodsService.createNewGood(goodEntity);
        return ResponseEntity
                .created(assembler.toModel(createdGood).getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(assembler.toModel(createdGood));
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return new ResponseEntity<>(assembler.toModel(goodsService.getOneGood(id)), HttpStatus.OK);
    }

    @PutMapping("/goods/{id}")
    @Transactional
    public ResponseEntity<?> replaceGood(@RequestBody GoodEntity newGood, @PathVariable Long id) {
        EntityModel<GoodEntity> goodEntityModel = assembler.toModel(goodsService.updateGood(id, newGood));
        return ResponseEntity
                .created(goodEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(goodEntityModel);
    }

    @DeleteMapping("/goods/{id}")
    @Transactional
    public ResponseEntity<?> deleteGood(@PathVariable Long id) {
        goodsService.deleteGood(id);
        return ResponseEntity.noContent().build();
    }
}
