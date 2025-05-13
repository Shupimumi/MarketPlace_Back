package com.home.marketplace.services;

import com.home.marketplace.assemblers.GoodEntityModelAssembler;
import com.home.marketplace.controllers.GoodsController;
import com.home.marketplace.controllers.exceptions.GoodNotFoundException;
import com.home.marketplace.db.entities.GoodEntity;
import com.home.marketplace.db.repositories.GoodsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class GoodsService {
    @PersistenceContext
    EntityManager entityManager;

    private final GoodEntityModelAssembler assembler;
    private final GoodsRepository goodsRepository;

    GoodsService(GoodsRepository goodsRepository, GoodEntityModelAssembler assembler) {
        this.assembler = assembler;
        this.goodsRepository = goodsRepository;
    }

    public ResponseEntity<?> getAllGoods() {
        List<EntityModel<GoodEntity>> goods = entityManager
                .createQuery("FROM GoodEntity", GoodEntity.class)
                .getResultStream()
                .map(assembler::toModel)
                .toList();
        return new ResponseEntity<>(CollectionModel.of(goods, linkTo(methodOn(GoodsController.class).all()).withSelfRel()), HttpStatus.OK);
    }

    public List<GoodEntity> getAllGoodsById(List<Long> ids) {
        return goodsRepository.findAllById(ids);
    }

    public GoodEntity getOneGood(Long id) {
        try {
            return entityManager.createQuery("SELECT g FROM GoodEntity g WHERE g.id = :id", GoodEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException error) {
            throw new GoodNotFoundException(id);
        }
    }

    public void deleteGood(Long id) {
        try {
            GoodEntity goodEntity = entityManager.find(GoodEntity.class, id);
            entityManager.remove(goodEntity);
        } catch (IllegalArgumentException error) {
            throw new GoodNotFoundException(id);
        }
    }

    public GoodEntity createNewGood(GoodEntity goodEntity) {
        entityManager.persist(goodEntity);
        return getOneGood(goodEntity.getId());
    }

    public GoodEntity updateGood(Long id, GoodEntity newGood) {
        try {
            GoodEntity oldGoodEntity = entityManager.find(GoodEntity.class, id);
            newGood.setId(oldGoodEntity.getId());
            entityManager.merge(newGood);
            return getOneGood(newGood.getId());
        } catch (NullPointerException error) {
            throw new GoodNotFoundException(id);
        }
    }
}
