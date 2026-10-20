package com.home.marketplace.db.repositories;

import com.home.marketplace.db.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
