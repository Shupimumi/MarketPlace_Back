package com.home.marketplace.db.repositories;

import com.home.marketplace.db.entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}
