package com.home.marketplace.controllers;

import com.home.marketplace.assemblers.GoodEntityModelAssembler;
import com.home.marketplace.controllers.exceptions.GoodNotFoundException;
import com.home.marketplace.db.entities.GoodEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class GoodsController {
    @PersistenceContext
    private EntityManager entityManager;

    private final GoodEntityModelAssembler assembler;

    @Autowired
    GoodsController(GoodEntityModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/goods")
    public ResponseEntity<?> all() {
        List<EntityModel<GoodEntity>> goods = entityManager
                .createQuery("FROM GoodEntity", GoodEntity.class)
                .getResultStream()
                .map(assembler::toModel)
                .toList();
        return new ResponseEntity<>(CollectionModel.of(goods, linkTo(methodOn(GoodsController.class).all()).withSelfRel()), HttpStatus.OK);
    }

    @PostMapping("/goods")
    @Transactional
    public ResponseEntity<?> newGood(@RequestBody GoodEntity goodEntity) {
        entityManager.persist(goodEntity);
        return ResponseEntity
                .created(assembler.toModel(goodEntity).getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(assembler.toModel(goodEntity));
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        try {
            GoodEntity goodEntity = entityManager.createQuery("SELECT g FROM GoodEntity g WHERE g.id = :id", GoodEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return new ResponseEntity<>(assembler.toModel(goodEntity), HttpStatus.OK);
        } catch (NoResultException error) {
            throw new GoodNotFoundException(id);
        }
    }

    @PutMapping("/goods/{id}")
    @Transactional
    public ResponseEntity<?> replaceGood(@RequestBody GoodEntity newGood, @PathVariable Long id) {
        try {
            GoodEntity oldGoodEntity = entityManager.find(GoodEntity.class, id);
            newGood.setId(oldGoodEntity.getId());
            entityManager.merge(newGood);
            EntityModel<GoodEntity> goodEntityModel = assembler.toModel(newGood);

            return ResponseEntity
                    .created(goodEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(goodEntityModel);
        } catch (NullPointerException error) {
            throw new GoodNotFoundException(id);
        }
    }

    @DeleteMapping("/goods/{id}")
    @Transactional
    public ResponseEntity<?> deleteGood(@PathVariable Long id) {
        try {
            GoodEntity goodEntity = entityManager.find(GoodEntity.class, id);
            entityManager.remove(goodEntity);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException error) {
            throw new GoodNotFoundException(id);
        }
    }
}
