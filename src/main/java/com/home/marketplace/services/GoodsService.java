package com.home.marketplace.services;

import com.home.marketplace.assemblers.GoodEntityModelAssembler;
import com.home.marketplace.controllers.GoodsController;
import com.home.marketplace.controllers.exceptions.GoodNotFoundException;
import com.home.marketplace.db.entities.Good;
import com.home.marketplace.db.repositories.GoodRepository;
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
    private final GoodRepository goodRepository;

    GoodsService(GoodRepository goodRepository, GoodEntityModelAssembler assembler) {
        this.assembler = assembler;
        this.goodRepository = goodRepository;
    }

    public ResponseEntity<?> getAllGoods() {
        List<EntityModel<Good>> goods = entityManager
                .createQuery("FROM GoodEntity", Good.class)
                .getResultStream()
                .map(assembler::toModel)
                .toList();
        return new ResponseEntity<>(CollectionModel.of(goods, linkTo(methodOn(GoodsController.class).all()).withSelfRel()), HttpStatus.OK);
    }

    public List<Good> getAllGoodsById(List<Long> ids) {
        return goodRepository.findAllById(ids);
    }

    public Good getOneGood(Long id) {
        try {
            return entityManager.createQuery("SELECT g FROM GoodEntity g WHERE g.id = :id", Good.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException error) {
            throw new GoodNotFoundException(id);
        }
    }

    public void deleteGood(Long id) {
        try {
            Good good = entityManager.find(Good.class, id);
            entityManager.remove(good);
        } catch (IllegalArgumentException error) {
            throw new GoodNotFoundException(id);
        }
    }

    public Good createNewGood(Good good) {
        entityManager.persist(good);
        return getOneGood(good.getId());
    }

    public Good updateGood(Long id, Good newGood) {
        try {
            Good oldGood = entityManager.find(Good.class, id);
            newGood.setId(oldGood.getId());
            entityManager.merge(newGood);
            return getOneGood(newGood.getId());
        } catch (NullPointerException error) {
            throw new GoodNotFoundException(id);
        }
    }
}
