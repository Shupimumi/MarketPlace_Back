package com.home.marketplace.db.repositories;

import com.home.marketplace.db.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Set<OrderEntity> findBy
}
